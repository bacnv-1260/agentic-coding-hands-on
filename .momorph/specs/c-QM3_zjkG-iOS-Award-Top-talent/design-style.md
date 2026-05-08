# Design Style: [iOS] Award_Top Talent

**Screen ID**: `c-QM3_zjkG`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/c-QM3_zjkG

---

## Screen Layout

- **Platform**: Android mobile (375 × 812dp reference)
- **Background**: Dark (`#00101A`) với artwork keyvisual overlay (abstract colorful art image `24d4f0821c47c4be76839d3c530f3807.png`) ở phần trên
- **Scroll**: Dọc — toàn bộ `mms_2_content` cuộn; Header + NavBar sticky
- **Safe area**: Header tôn trọng status bar insets; NavBar tôn trọng nav bar insets

---

## Colors

| Token | Hex | Dùng ở |
|-------|-----|--------|
| Background | `#00101A` | Nền toàn màn hình |
| ButtonPrimaryBg / Gold | `#FFEA9E` | Tên giải thưởng, giá trị tiền, label giải, nút "Chi tiết" text/border |
| TextOnDark | `#FFFFFF` (hoặc off-white) | Mô tả, label phụ, text thông thường |
| TextSecondary | `#A0AEC0` (ước lượng) | "Sun\* Annual Awards 2025" label nhỏ, "cho mỗi giải thưởng" |
| BorderGold | `#FFEA9E` với alpha thấp | Border card huy hiệu giải thưởng |
| NotificationBadge / Red | `#DA251D` | Badge chuông thông báo |
| NavBarBackground | `#0A1929` (dark navy) | Background Bottom Nav |
| NavBarActiveColor | `#FFEA9E` (gold) | Tab đang active (icon + text) |
| NavBarInactiveColor | `#6B7280` | Tab không active |
| DropdownBackground | Surface dark | Background dropdown chọn hạng mục |

---

## Typography

| Element | Font | Weight | Size | Color |
|---------|------|--------|------|-------|
| Kudos banner subtitle | Montserrat | Regular | 12sp | TextOnDark |
| Kudos banner title "KUDOS" | Montserrat | Bold / ExtraBold | 28–32sp | `#FFEA9E` (gold) |
| "Sun\* Annual Awards 2025" label | Montserrat | Regular | 12sp | TextSecondary |
| "Hệ thống giải thưởng SAA 2025" | Montserrat | Bold | 24–28sp | TextOnDark |
| Dropdown trigger text | Montserrat | SemiBold | 14sp | TextOnDark |
| Award title "Top Talent" | Montserrat | Bold | 18–20sp | `#FFEA9E` |
| Award description | Montserrat | Regular | 14sp | TextOnDark |
| Section label ("Số lượng giải thưởng") | Montserrat | SemiBold | 14sp | TextOnDark |
| Quantity value "10" | Montserrat | Bold | 20–24sp | TextOnDark |
| Quantity unit "Cá nhân" | Montserrat | Regular | 14sp | TextSecondary |
| Prize value "7.000.000 VNĐ" | Montserrat | Bold | 20–24sp | `#FFEA9E` |
| Prize note "cho mỗi giải thưởng" | Montserrat | Regular | 12–14sp | TextSecondary |
| "Phong trào ghi nhận" label | Montserrat | Regular | 12sp | TextSecondary |
| "Sun\* Kudos" section title | Montserrat | Bold | 22–24sp | `#FFEA9E` |
| "ĐIỂM MỚI CỦA SAA 2025" badge | Montserrat | Bold | 12sp | TextOnDark |
| Kudos description | Montserrat | Regular | 14sp | TextOnDark |
| "Chi tiết" button text | Montserrat | SemiBold | 14–16sp | `#FFEA9E` |
| Bottom Nav label | Montserrat | Medium | 10–12sp | NavBarActiveColor / NavBarInactiveColor |

---

## Component Specs

### 1. Header (`mms_1_header`)

- **Height**: ~96dp (bao gồm status bar ~44dp + content 52dp)
- **Background**: Gradient dọc từ `#00101A` đến transparent (mờ dần xuống)
- **Position**: Sticky top (fixed overlay)
- **Logo**: `img_saa_logo` — 48×44dp, căn trái
- **Actions row** (căn phải, gap 10dp):
  - Language switcher: flag emoji + text "VN" + dropdown arrow, height 32dp, radius 4dp, padding `(8,4,8,4)`
  - Search icon: 24×24dp, tint white
  - Notification bell: 24×24dp, tint white; badge dot 8×8dp màu `#DA251D` ở top-right khi `unreadCount > 0`

### 2. Kudos Banner (`mms_A_KV Kudos`)

- **Background**: Transparent / overlaps keyvisual artwork
- **Padding**: horizontal 20dp, top ~20dp
- **Subtitle**: "Hệ thống ghi nhận và cảm ơn" — 12sp, TextSecondary
- **Logo**: flame icon SVG (`3d624d521fdc4d48a32a0892d2a8f1eb.svg`) + "KUDOS" text — 28–32sp Bold, gold

### 3. Award Highlight Block (`mms_B_Highlight`)

- **Background**: Semi-transparent dark overlay trên keyvisual
- **Padding**: horizontal 20dp, vertical 24dp
- Layout dọc, gap 12dp:
  - **Sub-label**: "Sun\* Annual Awards 2025" — 12sp, TextSecondary
  - **Title**: "Hệ thống giải thưởng SAA 2025" — 24–28sp Bold, white
  - **Dropdown**: width ~140dp, height 40dp, border 1dp gold, radius 4dp
    - Content: text tên hạng mục + `KeyboardArrowDown` icon
    - Background: `#0A1929` dark

### 4. Award Image (`mms_2.3_award` — image part)

- **Container**: ~240×240dp, centered horizontally
- **Image**: award-specific circular badge graphic loaded from API `image_url` — badge background (`0ef5f5b22e406adbf9630ac04be95269.png`) + badge overlay with award name text (e.g. "TOP PROJECT", "TOP TALENT" — depends on selected award)
- **Border**: subtle gold glow/border (1dp, `#FFEA9E` 30% alpha), radius 16dp
- **Note**: The static Figma frame displays "TOP PROJECT" award as example; actual content is dynamic from API

### 5. Award Info Block (`mms_2.3_award` — text part)

- **Padding**: horizontal 20dp, gap giữa sections 24dp
- **Dividers**: 1dp horizontal line, color `#FFFFFF20`
- **Award title row**: icon (SVG `9650de8178a7c4a52a9ea4aa0fe48988.svg` 20×20dp) + text "Top Talent" — 18–20sp Bold, gold; spacing 8dp
- **Description**: multiline, 14sp Regular, white, lineHeight 1.5×
- **Quantity section**:
  - Icon (`34faad1f6bf2be1982d3aa5e465c2914.svg` 20dp) + label "Số lượng giải thưởng" — 14sp SemiBold, white
  - Value row: "**10**" (20–24sp Bold, white) + "Cá nhân" (14sp Regular, TextSecondary)
- **Prize section**:
  - Icon (`05359403b53021740e42e425a245ba9e.svg` 20dp) + label "Giá trị giải thưởng" — 14sp SemiBold, white
  - Value row: "**7.000.000 VNĐ**" (20–24sp Bold, gold) + "cho mỗi giải thưởng" (12sp Regular, TextSecondary)

### 6. Sun\*Kudos Block (`mms_2.4_kudos`)

- **Padding**: horizontal 20dp, vertical 32dp
- **Layout dọc**, gap 12dp:
  - Label "Phong trào ghi nhận" — 12sp Regular, TextSecondary
  - Title "Sun\* Kudos" — 22–24sp Bold, gold
  - Banner image: `235615492e296823bd76574065c3dd3c.png` (Kudos banner) + `444ca963317bf5e9b2af96b369a76ced.png` — width fill, height ~120dp, radius 8dp
  - Badge "ĐIỂM MỚI CỦA SAA 2025" — uppercase, 12sp Bold
  - Description: multiline, 14sp Regular, TextSecondary
  - **"Chi tiết" button**: width 120dp, height 40dp, radius 4dp, **background `#FFEA9E` (gold filled)**; text "Chi tiết" + arrow icon; text color `#0A1929` (dark, on gold bg) — **NOT outlined**

### 7. Bottom Navigation Bar (`mms_3_nav bar`)

- **Height**: 64dp + navigation bar insets
- **Background**: `#0A1929` (dark navy)
- **4 tabs**: SAA 2025 (home icon) / Awards (trophy icon — **active**) / Kudos (star icon) / Profile (person icon)
- **Active tab**: icon + text color `#FFEA9E` (gold)
- **Inactive tab**: color `#6B7280` (grey)
- **Position**: Sticky bottom (fixed overlay)

---

## Assets Required

| Asset | Node ID | Format | Dùng ở |
|-------|---------|--------|--------|
| Keyvisual BG artwork | `6885:10261` | PNG | Background artwork overlay |
| SAA Logo | `I6885:10264;88:1827` | PNG | Header logo |
| Search icon | `I6885:10264;88:1869` | SVG | Header action |
| Notification bell | `I6885:10264;88:1830` | SVG | Header action |
| Kudos logo SVG | `6885:10271` | SVG | Kudos banner |
| Award badge BG | `6885:10293` (bg rect) | PNG | Award image container |
| Award badge overlay | `I6885:10293;72:2080;10:951` | PNG | Award image overlay (dynamic — award-specific asset from `image_url`) |
| Award title icon | `6885:10296` | SVG | "Top Talent" tiêu đề |
| Quantity icon | `6885:10302` | SVG | "Số lượng giải thưởng" |
| Prize icon | `6885:10310` | SVG | "Giá trị giải thưởng" |
| Kudos banner 1 | `6885:10317` | PNG | Kudos block |
| Kudos banner 2 | `6885:10319` | PNG | Kudos block |
| "Chi tiết" arrow icon | `6885:10321` | SVG | Kudos block button |
| Nav bar icons × 4 | `I6885:10332;75:2033–2042` | SVG | Bottom nav |

---

## Spacing & Layout Summary

```
Screen (375dp wide)
├── [sticky top] Header — height ~96dp (status bar + 52dp content)
└── LazyColumn (contentPadding bottom = 64dp + nav insets)
    ├── mms_A_KV Kudos — padding(horizontal=20, top=20)
    ├── mms_B_Highlight — padding(horizontal=20, vertical=24)
    │   ├── Sub-label 12sp
    │   ├── Title 24–28sp
    │   └── Dropdown — width 140dp, height 40dp
    ├── mms_2.3_award — padding(horizontal=20, vertical=24)
    │   ├── Badge image — 240×240dp, centered
    │   ├── Divider
    │   ├── Award title row (icon + text)
    │   ├── Description (multiline)
    │   ├── Divider
    │   ├── Quantity section (label + value row)
    │   ├── Divider
    │   └── Prize section (label + value row)
    └── mms_2.4_kudos — padding(horizontal=20, vertical=32)
        ├── Label 12sp
        ├── Title 22–24sp
        ├── Banner image — fill width, height ~120dp
        ├── "ĐIỂM MỚI" badge
        ├── Description (multiline)
        └── "Chi tiết" button — width 120dp, height 40dp
[sticky bottom] Bottom Nav Bar — height 64dp + nav insets
```
