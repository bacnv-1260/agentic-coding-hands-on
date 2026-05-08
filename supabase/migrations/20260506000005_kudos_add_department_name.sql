-- Add denormalized department name columns to kudos for display purposes
alter table public.kudos
    add column if not exists sender_department_name   text,
    add column if not exists recipient_department_name text;

select pg_notify('pgrst', 'reload schema');
