-- Seed: create required storage buckets

INSERT INTO storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
VALUES (
    'kudos-images',
    'kudos-images',
    false,
    10485760, -- 10 MB
    ARRAY['image/jpeg', 'image/png', 'image/webp', 'image/gif']
)
ON CONFLICT (id) DO NOTHING;

-- RLS: authenticated users can upload to kudos-images
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_policies
        WHERE tablename = 'objects' AND policyname = 'kudos_images_insert'
    ) THEN
        CREATE POLICY "kudos_images_insert" ON storage.objects
            FOR INSERT TO authenticated
            WITH CHECK (bucket_id = 'kudos-images');
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_policies
        WHERE tablename = 'objects' AND policyname = 'kudos_images_select'
    ) THEN
        CREATE POLICY "kudos_images_select" ON storage.objects
            FOR SELECT TO authenticated
            USING (bucket_id = 'kudos-images');
    END IF;
END;
$$;
