-- Migration: create awards and notifications tables

-- Awards table
create table if not exists public.awards (
    id          uuid primary key default gen_random_uuid(),
    name        text,
    description text,
    category    text,
    image_url   text,
    created_at  timestamptz not null default now()
);

-- Enable RLS
alter table public.awards enable row level security;

-- Allow read access to authenticated users
create policy "awards_select_authenticated"
    on public.awards for select
    to authenticated
    using (true);

-- Notifications table
create table if not exists public.notifications (
    id         uuid primary key default gen_random_uuid(),
    user_id    uuid references auth.users(id) on delete cascade,
    title      text,
    body       text,
    is_read    boolean not null default false,
    created_at timestamptz not null default now()
);

-- Enable RLS
alter table public.notifications enable row level security;

-- Allow users to read only their own notifications
create policy "notifications_select_own"
    on public.notifications for select
    to authenticated
    using (user_id = auth.uid());

-- Storage: create public bucket for award images
insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values ('awards', 'awards', true, 52428800, array['image/png', 'image/jpeg'])
on conflict (id) do update set public = true;
