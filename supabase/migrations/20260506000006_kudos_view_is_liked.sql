-- Create a view that computes is_liked per authenticated user from kudos_likes.
-- The static kudos.is_liked column always defaults to false and is never updated
-- by like/unlike operations, causing duplicate key violations on re-insert after reload.
-- Using security_invoker ensures the underlying kudos RLS policy still applies.

create or replace view public.kudos_view
with (security_invoker = true)
as
select
    k.id,
    k.sender_id,
    k.recipient_id,
    k.message,
    k.award_category_name,
    k.heart_count,
    k.created_at,
    k.sender_avatar_url,
    k.sender_name,
    k.sender_employee_code,
    k.sender_badge_type,
    k.sender_department_name,
    k.recipient_avatar_url,
    k.recipient_name,
    k.recipient_hero_tier,
    k.recipient_department_name,
    k.share_url,
    k.photo_urls,
    k.is_anonymous,
    k.anonymous_nickname,
    k.can_like,
    exists(
        select 1
        from public.kudos_likes kl
        where kl.kudos_id = k.id
          and kl.user_id = auth.uid()
    ) as is_liked
from public.kudos k;

grant select on public.kudos_view to authenticated;
