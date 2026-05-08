-- Dev seed: awards table
-- Images are served from Supabase Storage (local: http://10.0.2.2:54321)

insert into public.awards (name, description, category, image_url, quantity, quantity_unit, prize_value) values
  (
    'Top Talent',
    'Vinh danh top cá nhân xuất sắc trên mọi phương diện — những Sunner không chỉ hoàn thành tốt công việc mà còn lan tỏa năng lượng tích cực và truyền cảm hứng cho đồng đội.',
    'Individual',
    'http://10.0.2.2:54321/storage/v1/object/public/awards/award1.png',
    10,
    'Cá nhân',
    '7.000.000 VNĐ'
  ),
  (
    'Top Project',
    'Ghi nhận dự án xuất sắc nhất năm — đội nhóm đã mang lại giá trị vượt trội cho khách hàng và thể hiện tinh thần hợp tác mẫu mực.',
    'Team',
    'http://10.0.2.2:54321/storage/v1/object/public/awards/award2.png',
    2,
    'Tập thể',
    '15.000.000 VNĐ'
  ),
  (
    'Culture Champion',
    'Tôn vinh cá nhân lan toả văn hoá Sun* mạnh mẽ nhất — người luôn sống đúng giá trị cốt lõi và truyền cảm hứng cho những người xung quanh.',
    'Culture',
    'http://10.0.2.2:54321/storage/v1/object/public/awards/award3.png',
    5,
    'Cá nhân',
    '5.000.000 VNĐ'
  )
on conflict do nothing;
