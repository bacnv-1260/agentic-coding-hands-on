-- Migration: create profiles table (user department mapping)
-- profiles mirrors auth.users 1-to-1; holds non-auth metadata like department

create table if not exists public.profiles (
    id            uuid primary key references auth.users(id) on delete cascade,
    department_id uuid references public.departments(id) on delete set null,
    full_name     text,
    employee_code text,
    avatar_url    text,
    badge_type    text,
    hero_tier     int not null default 0,
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now()
);

alter table public.profiles enable row level security;

create policy "profiles_select_authenticated"
    on public.profiles for select
    to authenticated
    using (true);

create policy "profiles_update_own"
    on public.profiles for update
    to authenticated
    using (auth.uid() = id);

-- Auto-create profile row on new user signup
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer set search_path = public
as $$
begin
    insert into public.profiles (id, full_name, avatar_url)
    values (
        new.id,
        coalesce(new.raw_user_meta_data->>'full_name', ''),
        coalesce(new.raw_user_meta_data->>'avatar_url', '')
    )
    on conflict (id) do nothing;
    return new;
end;
$$;

create or replace trigger on_auth_user_created
    after insert on auth.users
    for each row execute procedure public.handle_new_user();

select pg_notify('pgrst', 'reload schema');
