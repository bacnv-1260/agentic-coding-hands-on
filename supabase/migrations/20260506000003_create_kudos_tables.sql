-- Migration: create kudos-related tables

-- Hashtags
create table if not exists public.hashtags (
    id   uuid primary key default gen_random_uuid(),
    name text unique not null
);

alter table public.hashtags enable row level security;

create policy "hashtags_select_authenticated"
    on public.hashtags for select
    to authenticated
    using (true);

-- Departments
create table if not exists public.departments (
    id   uuid primary key default gen_random_uuid(),
    name text unique not null
);

alter table public.departments enable row level security;

create policy "departments_select_authenticated"
    on public.departments for select
    to authenticated
    using (true);

-- Kudos table
create table if not exists public.kudos (
    id                    uuid primary key default gen_random_uuid(),
    sender_id             uuid references auth.users(id) on delete set null,
    recipient_id          uuid references auth.users(id) on delete cascade,
    message               text,
    award_category_name   text,
    heart_count           int not null default 0,
    created_at            timestamptz not null default now(),
    sender_avatar_url     text,
    sender_name           text,
    sender_employee_code  text,
    sender_badge_type     text,
    recipient_avatar_url  text,
    recipient_name        text,
    recipient_hero_tier   int not null default 0,
    share_url             text,
    is_liked              boolean not null default false,
    photo_urls            text[] not null default '{}',
    is_anonymous          boolean not null default false,
    anonymous_nickname    text not null default '',
    can_like              boolean not null default true
);

alter table public.kudos enable row level security;

create policy "kudos_select_authenticated"
    on public.kudos for select
    to authenticated
    using (true);

create policy "kudos_insert_own"
    on public.kudos for insert
    to authenticated
    with check (sender_id = auth.uid());

create policy "kudos_update_own"
    on public.kudos for update
    to authenticated
    using (sender_id = auth.uid() or recipient_id = auth.uid());

-- Kudos ↔ Hashtags join table
create table if not exists public.kudos_hashtags (
    kudos_id   uuid references public.kudos(id) on delete cascade,
    hashtag_id uuid references public.hashtags(id) on delete cascade,
    primary key (kudos_id, hashtag_id)
);

alter table public.kudos_hashtags enable row level security;

create policy "kudos_hashtags_select_authenticated"
    on public.kudos_hashtags for select
    to authenticated
    using (true);

create policy "kudos_hashtags_insert_own"
    on public.kudos_hashtags for insert
    to authenticated
    with check (
        exists (
            select 1 from public.kudos
            where id = kudos_id and sender_id = auth.uid()
        )
    );

-- Kudos likes
create table if not exists public.kudos_likes (
    kudos_id uuid references public.kudos(id) on delete cascade,
    user_id  uuid references auth.users(id) on delete cascade,
    primary key (kudos_id, user_id)
);

alter table public.kudos_likes enable row level security;

create policy "kudos_likes_select_authenticated"
    on public.kudos_likes for select
    to authenticated
    using (true);

create policy "kudos_likes_insert_own"
    on public.kudos_likes for insert
    to authenticated
    with check (user_id = auth.uid());

create policy "kudos_likes_delete_own"
    on public.kudos_likes for delete
    to authenticated
    using (user_id = auth.uid());

-- User stats (materialized per user)
create table if not exists public.user_stats (
    id                     uuid primary key references auth.users(id) on delete cascade,
    kudos_received         int not null default 0,
    kudos_sent             int not null default 0,
    hearts_received        int not null default 0,
    secret_boxes_opened    int not null default 0,
    secret_boxes_unopened  int not null default 0,
    updated_at             timestamptz not null default now()
);

alter table public.user_stats enable row level security;

create policy "user_stats_select_own"
    on public.user_stats for select
    to authenticated
    using (id = auth.uid());

-- Secret boxes
create table if not exists public.secret_boxes (
    id         uuid primary key default gen_random_uuid(),
    user_id    uuid references auth.users(id) on delete cascade,
    opened     boolean not null default false,
    created_at timestamptz not null default now()
);

alter table public.secret_boxes enable row level security;

create policy "secret_boxes_select_own"
    on public.secret_boxes for select
    to authenticated
    using (user_id = auth.uid());

create policy "secret_boxes_update_own"
    on public.secret_boxes for update
    to authenticated
    using (user_id = auth.uid());

-- Gift recipients log
create table if not exists public.gift_recipients (
    id          uuid primary key default gen_random_uuid(),
    user_id     uuid references auth.users(id) on delete cascade,
    full_name   text,
    avatar_url  text,
    reward_name text,
    created_at  timestamptz not null default now()
);

alter table public.gift_recipients enable row level security;

create policy "gift_recipients_select_authenticated"
    on public.gift_recipients for select
    to authenticated
    using (true);

-- Enable realtime for kudos
alter publication supabase_realtime add table public.kudos;

-- Reload PostgREST schema cache so new tables are immediately accessible
select pg_notify('pgrst', 'reload schema');
