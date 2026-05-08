-- Dev seed: kudos tables
-- Matches the Sun*Kudos UI (screenId: fO0Kt19sZZ)

-- ─── Hashtags ──────────────────────────────────────────────────────────────
insert into public.hashtags (id, name) values
  ('11111111-0001-0000-0000-000000000001', 'teamwork'),
  ('11111111-0001-0000-0000-000000000002', 'innovation'),
  ('11111111-0001-0000-0000-000000000003', 'leadership'),
  ('11111111-0001-0000-0000-000000000004', 'customer_first'),
  ('11111111-0001-0000-0000-000000000005', 'ownership'),
  ('11111111-0001-0000-0000-000000000006', 'growth_mindset')
on conflict (id) do nothing;

-- ─── Departments ──────────────────────────────────────────────────────────
insert into public.departments (id, name) values
  ('22222222-0001-0000-0000-000000000001', 'Engineering'),
  ('22222222-0001-0000-0000-000000000002', 'Design'),
  ('22222222-0001-0000-0000-000000000003', 'Product'),
  ('22222222-0001-0000-0000-000000000004', 'Marketing'),
  ('22222222-0001-0000-0000-000000000005', 'HR')
on conflict (id) do nothing;

-- ─── Kudos (5 highlight + 8 feed = 13 total) ──────────────────────────────
-- Highlight kudos (top heart_count — shown in carousel)
insert into public.kudos (
  id, sender_id, recipient_id, message, award_category_name,
  heart_count, created_at,
  sender_name, sender_employee_code, sender_badge_type, sender_avatar_url,
  recipient_name, recipient_hero_tier, recipient_avatar_url,
  share_url, is_liked, photo_urls, is_anonymous, anonymous_nickname, can_like
) values
  (
    'aaaaaaaa-0001-0000-0000-000000000001',
    null, null,
    'Cảm ơn Xuân đã hỗ trợ team vượt qua sprint khó nhất quý này! Không có bạn thì chắc chắn chúng mình không về đích đúng hạn được.',
    'TEAMWORK HERO',
    42, now() - interval '2 hours',
    'Nguyễn Văn Bắc', 'CECV10', 'gold',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=bacnv',
    'Huỳnh Dương Xuân', 50,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=xuan',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0001-0000-0000-000000000001',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0001-0000-0000-000000000002',
    null, null,
    'Minh đã dành cả cuối tuần để review code cho junior members mà không một lời than phiền. Đây chính xác là tinh thần ownership mà mình trân trọng nhất.',
    'OWNERSHIP STAR',
    38, now() - interval '5 hours',
    'Trần Thị Lan', 'CECV22', 'silver',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=lantran',
    'Phạm Hoàng Minh', 20,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=minh',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0001-0000-0000-000000000002',
    true, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0001-0000-0000-000000000003',
    null, null,
    'Linh đã present solution mới trước khách hàng Japan với confidence tuyệt vời. Client feedback cực kỳ tích cực và họ đã extend contract thêm 6 tháng!',
    'CUSTOMER FIRST',
    35, now() - interval '1 day',
    'Đỗ Quang Huy', 'CECV35', '',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=huy',
    'Vũ Thị Linh', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=linh',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0001-0000-0000-000000000003',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0001-0000-0000-000000000004',
    null, null,
    'Nam đã tự học thêm Rust trong 2 tháng và apply ngay vào performance-critical module. Kết quả: latency giảm 60%. Đây là growth mindset thực sự!',
    'GROWTH MINDSET',
    29, now() - interval '2 days',
    'Lê Thị Mai', 'CECV41', 'bronze',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=mai',
    'Bùi Đức Nam', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=namduc',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0001-0000-0000-000000000004',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0001-0000-0000-000000000005',
    null, null,
    'Một người Sunner bí ẩn muốn gửi lời cảm ơn đến Trang vì đã luôn sẵn sàng giúp đỡ bất kỳ ai cần. Bạn là ánh sáng của team! ✨',
    'CULTURE CHAMPION',
    24, now() - interval '3 days',
    '', 'ANON', '',
    null,
    'Nguyễn Minh Trang', 20,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=trang',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0001-0000-0000-000000000005',
    false, '{}', true, '🌟 Sunner bí ẩn', true
  ),

-- Feed kudos (lower heart_count — shown in All Kudos list)
  (
    'aaaaaaaa-0002-0000-0000-000000000001',
    null, null,
    'Cảm ơn Khoa đã fix bug production lúc 11 giờ đêm khi cả team đang hoảng loạn. Bạn là hero thực sự!',
    'TEAMWORK HERO',
    18, now() - interval '4 days',
    'Phan Thị Hồng', 'CECV55', '',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=hong',
    'Nguyễn Thành Khoa', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=khoa',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000001',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000002',
    null, null,
    'Thu đã mentor 3 bạn fresher trong tháng này với sự kiên nhẫn và tâm huyết đáng kinh ngạc. Cảm ơn bạn đã góp phần xây dựng thế hệ Sunner mới!',
    'LEADERSHIP',
    15, now() - interval '5 days',
    'Võ Minh Tuấn', 'CECV62', 'silver',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=tuan',
    'Đinh Thị Thu', 20,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=thu',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000002',
    true, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000003',
    null, null,
    'Quân đã đề xuất automation pipeline mới giúp release time giảm từ 2 giờ xuống còn 15 phút. Innovation nhỏ nhưng impact rất lớn!',
    'INNOVATION',
    12, now() - interval '6 days',
    'Trịnh Văn Dũng', 'CECV71', '',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=dung',
    'Lý Thành Quân', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=quan',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000003',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000004',
    null, null,
    'Phương đã tổ chức workshop internal về AI tools cho cả team. Rất nhiều bạn đã apply được ngay vào project thực tế. Cảm ơn bạn!',
    'GROWTH MINDSET',
    10, now() - interval '7 days',
    'Hoàng Anh Tuấn', 'CECV83', 'gold',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=tuananh',
    'Dương Thị Phương', 20,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=phuong',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000004',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000005',
    null, null,
    'Hải đã cover công việc cho mình suốt tuần khi mình nghỉ phép. Không có Hải thì project đã bị delay rồi. Cảm ơn bạn rất nhiều!',
    'TEAMWORK HERO',
    8, now() - interval '8 days',
    'Ngô Thị Bích', 'CECV91', '',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=bich',
    'Lê Minh Hải', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=hai',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000005',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000006',
    null, null,
    'Vy đã làm UX research với 20+ user interviews chỉ trong 1 tuần để kịp deadline design sprint. Sự tận tâm của bạn thực sự ấn tượng!',
    'CUSTOMER FIRST',
    7, now() - interval '9 days',
    'Bùi Quang Vinh', 'CECV99', '',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=vinh',
    'Trần Thị Mỹ Vy', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=myvy',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000006',
    false, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000007',
    null, null,
    'Long đã refactor legacy code module 5 năm tuổi mà không làm gián đoạn bất kỳ service nào. Đây là kỹ năng engineering đỉnh cao!',
    'OWNERSHIP STAR',
    5, now() - interval '10 days',
    'Phạm Thị Hà', 'CECV107', 'bronze',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=hapham',
    'Nguyễn Thanh Long', 10,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=long',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000007',
    true, '{}', false, '', true
  ),
  (
    'aaaaaaaa-0002-0000-0000-000000000008',
    null, null,
    'Một lời cảm ơn ẩn danh đến người đã luôn âm thầm chuẩn bị meeting notes cho cả team mà không ai biết. Bạn quan trọng hơn bạn nghĩ!',
    'CULTURE CHAMPION',
    3, now() - interval '11 days',
    '', 'ANON', '',
    null,
    'Cao Thị Lan Anh', 0,
    'https://api.dicebear.com/7.x/thumbs/svg?seed=lananh',
    'http://10.0.2.2:54321/kudos/aaaaaaaa-0002-0000-0000-000000000008',
    false, '{}', true, '💫 Người ngưỡng mộ', true
  )
on conflict (id) do nothing;

-- ─── Kudos ↔ Hashtags ─────────────────────────────────────────────────────
insert into public.kudos_hashtags (kudos_id, hashtag_id) values
  ('aaaaaaaa-0001-0000-0000-000000000001', '11111111-0001-0000-0000-000000000001'),
  ('aaaaaaaa-0001-0000-0000-000000000002', '11111111-0001-0000-0000-000000000005'),
  ('aaaaaaaa-0001-0000-0000-000000000003', '11111111-0001-0000-0000-000000000004'),
  ('aaaaaaaa-0001-0000-0000-000000000004', '11111111-0001-0000-0000-000000000006'),
  ('aaaaaaaa-0001-0000-0000-000000000005', '11111111-0001-0000-0000-000000000001'),
  ('aaaaaaaa-0002-0000-0000-000000000001', '11111111-0001-0000-0000-000000000001'),
  ('aaaaaaaa-0002-0000-0000-000000000002', '11111111-0001-0000-0000-000000000003'),
  ('aaaaaaaa-0002-0000-0000-000000000003', '11111111-0001-0000-0000-000000000002'),
  ('aaaaaaaa-0002-0000-0000-000000000004', '11111111-0001-0000-0000-000000000006'),
  ('aaaaaaaa-0002-0000-0000-000000000007', '11111111-0001-0000-0000-000000000005')
on conflict do nothing;

-- ─── Gift Recipients (D.3 — NHẬN QUÀ MỚI NHẤT) ────────────────────────────
insert into public.gift_recipients (id, user_id, full_name, avatar_url, reward_name, created_at) values
  (
    'cccccccc-0001-0000-0000-000000000001',
    null,
    'Huỳnh Dương Xuân',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=xuan',
    'Nhận được 1 áo phông SAA giới hạn',
    now() - interval '1 day'
  ),
  (
    'cccccccc-0001-0000-0000-000000000002',
    null,
    'Phạm Hoàng Minh',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=minh',
    'Nhận được 1 mug Sun* phiên bản đặc biệt',
    now() - interval '2 days'
  ),
  (
    'cccccccc-0001-0000-0000-000000000003',
    null,
    'Vũ Thị Linh',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=linh',
    'Nhận được voucher book 200.000 VNĐ',
    now() - interval '3 days'
  ),
  (
    'cccccccc-0001-0000-0000-000000000004',
    null,
    'Bùi Đức Nam',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=namduc',
    'Nhận được 1 balo Sun* cao cấp',
    now() - interval '4 days'
  ),
  (
    'cccccccc-0001-0000-0000-000000000005',
    null,
    'Nguyễn Minh Trang',
    'https://api.dicebear.com/7.x/thumbs/svg?seed=trang',
    'Nhận được voucher ăn trưa 300.000 VNĐ',
    now() - interval '5 days'
  )
on conflict (id) do nothing;
