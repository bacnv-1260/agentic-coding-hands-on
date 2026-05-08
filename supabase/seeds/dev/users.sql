-- Dev seed: test user accounts for local development
-- These users can be used to test recipient search and kudo sending

INSERT INTO auth.users (
    id, email, encrypted_password, email_confirmed_at,
    created_at, updated_at, raw_user_meta_data, aud, role
)
VALUES
    (
        'aaaaaaaa-0001-0000-0000-000000000001',
        'alice@example.com',
        crypt('password123', gen_salt('bf')),
        now(), now(), now(),
        '{"full_name":"Alice Tran"}'::jsonb,
        'authenticated', 'authenticated'
    ),
    (
        'aaaaaaaa-0002-0000-0000-000000000001',
        'bob@example.com',
        crypt('password123', gen_salt('bf')),
        now(), now(), now(),
        '{"full_name":"Bob Nguyen"}'::jsonb,
        'authenticated', 'authenticated'
    ),
    (
        'aaaaaaaa-0003-0000-0000-000000000001',
        'carol@example.com',
        crypt('password123', gen_salt('bf')),
        now(), now(), now(),
        '{"full_name":"Carol Le"}'::jsonb,
        'authenticated', 'authenticated'
    ),
    (
        'aaaaaaaa-0004-0000-0000-000000000001',
        'david@example.com',
        crypt('password123', gen_salt('bf')),
        now(), now(), now(),
        '{"full_name":"David Pham"}'::jsonb,
        'authenticated', 'authenticated'
    ),
    (
        'aaaaaaaa-0005-0000-0000-000000000001',
        'eva@example.com',
        crypt('password123', gen_salt('bf')),
        now(), now(), now(),
        '{"full_name":"Eva Hoang"}'::jsonb,
        'authenticated', 'authenticated'
    )
ON CONFLICT (id) DO NOTHING;

-- Profiles are auto-created by trigger on_auth_user_created,
-- but update department and employee_code here.
INSERT INTO public.profiles (id, full_name, department_id, employee_code)
VALUES
    ('aaaaaaaa-0001-0000-0000-000000000001', 'Alice Tran',  '22222222-0001-0000-0000-000000000001', 'EMP001'),
    ('aaaaaaaa-0002-0000-0000-000000000001', 'Bob Nguyen',  '22222222-0001-0000-0000-000000000002', 'EMP002'),
    ('aaaaaaaa-0003-0000-0000-000000000001', 'Carol Le',    '22222222-0001-0000-0000-000000000003', 'EMP003'),
    ('aaaaaaaa-0004-0000-0000-000000000001', 'David Pham',  '22222222-0001-0000-0000-000000000004', 'EMP004'),
    ('aaaaaaaa-0005-0000-0000-000000000001', 'Eva Hoang',   '22222222-0001-0000-0000-000000000005', 'EMP005')
ON CONFLICT (id) DO UPDATE SET
    full_name     = EXCLUDED.full_name,
    department_id = EXCLUDED.department_id,
    employee_code = EXCLUDED.employee_code;
