# Design Style: [iOS] Sun*Kudos

**Frame ID**: `6885:9059`
**Screen ID**: `fO0Kt19sZZ`
**Frame Name**: `[iOS] Sun*Kudos`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/fO0Kt19sZZ
**Extracted At**: 2026-05-06

---

## Design Tokens

### Colors

| Token Name | Hex Value | Opacity | Usage |
|------------|-----------|---------|-------|
| `--color-background` | `#00101A` | 100% | Screen background (toàn màn hình) |
| `--color-card-highlight-bg` | `#FFF8E1` | 100% | Background của B.3 KudosHighlightCard (light cream) |
| `--color-card-highlight-border` | `#FFEA9E` | 100% | Border 1px của B.3 Highlight card (gold) |
| `--color-card-post-bg` | `#FFFFFF` | 6% (`0F` hex) | Background của C.3 KudosPostCard (dark glass) |
| `--color-card-post-border` | `#FFFFFF` | 15% | Border của C.3 Post card |
| `--color-card-details-bg` | `#00070C` | 100% | Background của D.1 stats card và D.3 gift card |
| `--color-card-details-border` | `#998C5F` | 100% | Border ~0.8px của D.1/D.3 cards (golden brown) |
| `--color-write-btn-bg` | `#FFEA9E` | 10% | Background A.1 write kudos button (rgba(255,234,158,0.10)) |
| `--color-write-btn-border` | `#998C5F` | 100% | Border 1px A.1 write button (golden brown) |
| `--color-button-primary-bg` | `#FFEA9E` | 100% | Gold D.2 "Mở Secret Box" button bg |
| `--color-button-primary-text` | `#00101A` | 100% | Text trên primary button (D.2) |
| `--color-text-on-dark` | `#FFFFFF` | 100% | Body text trên nền tối |
| `--color-text-on-light` | `#00101A` | 100% | Text trên nền sáng (bên trong B.3 highlight card) |
| `--color-text-secondary` | `#FFFFFF` | 60% | Subtitle, timestamp, employee code |
| `--color-text-secondary-light` | `#999999` | 100% | Employee code trong B.3 card (gray) |
| `--color-text-gold` | `#FFEA9E` | 100% | "KUDOS" logo, section titles, stat values |
| `--color-divider-dark` | `#2E3940` | 100% | Divider trong D.1.5 và section headers |
| `--color-hashtag-highlight` | `#D4271D` | 100% | Hashtag text color trong B.3 Highlight card (red) |
| `--color-hashtag-post` | `#FFFFFF` | 60% | Hashtag text color trong C.3 Post card |
| `--color-badge-legend` | `#E73928` | 100% | Badge "Legend Hero" |
| `--color-badge-bg` | `#FFFFFF` | 12% | Background badge trong card |
| `--color-heart-active` | `#E73928` | 100% | Icon heart khi đã like |
| `--color-heart-inactive` | `#FFFFFF` | 60% | Icon heart khi chưa like |
| `--color-filter-button-bg` | `#FFFFFF` | 8% | Background filter dropdown buttons B.1.1 / B.1.2 |
| `--color-filter-button-border` | `#FFFFFF` | 20% | Viền filter button |
| `--color-filter-button-selected-bg` | `#FFEA9E` | 12% | Background filter khi đã chọn |
| `--color-filter-button-selected-border` | `#FFEA9E` | 60% | Border filter khi đã chọn |
| `--color-spotlight-node` | `#FFEA9E` | 100% | Node Sunner trên Spotlight Board |
| `--color-nav-active` | `#FFEA9E` | 100% | Active tab icon trong bottom nav |
| `--color-nav-inactive` | `#FFFFFF` | 40% | Inactive tab icon trong bottom nav |

### Typography

| Token Name | Font Family | Size | Weight | Line Height | Letter Spacing | Usage |
|------------|-------------|------|--------|-------------|----------------|-------|
| `--text-kudos-logo` | Montserrat | 48px | 800 (ExtraBold) | 52px | -0.5px | "KUDOS" text logo trong KV banner |
| `--text-kudos-subtitle` | Montserrat | 14px | 500 (Medium) | 20px | 0px | "Hệ thống ghi nhận và cảm ơn" dưới logo (gold, **không phải white**) |
| `--text-section-subtitle` | Montserrat | 12px | 400 (Regular) | 16px | 0px | "Sun* Annual Awards 2025" (section subtitles, white) |
| `--text-section-title` | Montserrat | 22px | 500 (Medium) | 28px | 0px | "HIGHLIGHT KUDOS", "SPOTLIGHT BOARD", "ALL KUDOS" (gold) |
| `--text-card-highlight-name` | Montserrat | 10px | 400 (Regular) | 16px | 0px | Tên người gửi/nhận trong B.3 Highlight card (dark on light bg) |
| `--text-card-highlight-code` | Montserrat | 10px | 500 (Medium) | 10px | 0.05px | Employee code trong B.3 card (gray #999) |
| `--text-card-highlight-hashtag` | Montserrat | 10px | 400 (Regular) | 11px | 0.23px | Hashtag text trong B.3 card (red #D4271D) |
| `--text-card-post-name` | Montserrat | 13px | 600 (SemiBold) | 18px | 0px | Tên người gửi/nhận trong C.3 Post card (white) |
| `--text-card-post-code` | Montserrat | 11px | 400 (Regular) | 14px | 0px | Employee code trong C.3 card (white @ 60%) |
| `--text-card-badge` | Montserrat | 10px | 500 (Medium) | 12px | 0.2px | Badge label (e.g., "Legend Hero") |
| `--text-card-timestamp` | Montserrat | 11px | 400 (Regular) | 14px | 0px | Thời gian đăng kudo |
| `--text-card-category` | Montserrat | 13px | 700 (Bold) | 16px | 0.3px | Recognition category title (e.g., "IDOL GIỚI TRẺ") |
| `--text-card-message` | Montserrat | 14px | 400 (Regular) | 20px | 0px | Nội dung kudo message |
| `--text-action-label` | Montserrat | 12px | 500 (Medium) | 16px | 0px | "Copy Link", "Xem chi tiết" action labels |
| `--text-heart-count` | Montserrat | 13px | 600 (SemiBold) | 16px | 0px | Heart count number |
| `--text-filter-label` | Montserrat | 13px | 400 (Regular) | 18px | 0px | Label filter dropdown (e.g., "Hashtag", "Phòng ban") |
| `--text-stats-label` | Montserrat | 14px | 300 (Light) | 20px | 0.25px | Label thống kê (white) |
| `--text-stats-value` | Montserrat | 14px | 700 (Bold) | 20px | 0.25px | Số liệu thống kê — right-aligned (gold #FFEA9E) |
| `--text-gift-title` | Montserrat | 14px | 700 (Bold) | 20px | 0px | Title D.3.1 "N SUNNER NHẬN QUÀ MỚI NHẤT" (gold, centered) |
| `--text-gift-name` | Montserrat | 14px | 600 (SemiBold) | 20px | 0px | Tên Sunner nhận quà |
| `--text-gift-reward` | Montserrat | 12px | 400 (Regular) | 16px | 0px | Mô tả phần thưởng |
| `--text-secret-box-btn` | Montserrat | 14px | 500 (Medium) | 20px | 0px | Label nút "Mở Secret Box" (dark #00101A) |
| `--text-write-placeholder` | Montserrat | 14px | 500 (Medium) | 20px | 0px | Placeholder trong A.1 input button (white, centered) |
| `--text-spotlight-count` | Montserrat | 16px | 700 (Bold) | 20px | 0px | "388 KUDOS" trong Spotlight Board |
| `--text-kudos-total-subtitle` | Montserrat | 10px | 400 (Regular) | 14px | 0px | "SAA KUDOS" dưới số đếm Spotlight |

### Spacing

| Token Name | Value | Usage |
|------------|-------|-------|
| `--spacing-screen-h-padding` | 16px | Horizontal padding của mọi section |
| `--spacing-section-gap` | 24px | Khoảng cách giữa các section chính (A→B→C→D) |
| `--spacing-card-padding-highlight` | 8px 12px | Internal padding của B.3 Highlight card |
| `--spacing-card-padding-post` | 16px | Internal padding của C.3 Post card |
| `--spacing-card-gap` | 8px | Gap giữa các element trong highlight card |
| `--spacing-card-radius-highlight` | 8px | Bo góc của B.3 Highlight card |
| `--spacing-card-radius-post` | 12px | Bo góc của C.3 Post card |
| `--spacing-avatar-size-highlight` | 24px | Avatar trong B.3 Highlight card (sender/recipient) |
| `--spacing-avatar-size-post` | 32px | Avatar trong C.3 All Kudos list |
| `--spacing-avatar-size-gift` | 32px | Avatar trong D.3 Gift recipient row |
| `--spacing-badge-h-padding` | 8px | Horizontal padding của badge tag |
| `--spacing-badge-v-padding` | 3px | Vertical padding của badge tag |
| `--spacing-badge-radius` | 4px | Border radius của badge tag |
| `--spacing-hashtag-h-padding` | 10px | Horizontal padding của hashtag chip |
| `--spacing-hashtag-v-padding` | 4px | Vertical padding của hashtag chip |
| `--spacing-hashtag-radius` | 16px | Border radius của hashtag chip (pill) |
| `--spacing-filter-btn-h-padding` | 12px | Horizontal padding nút filter |
| `--spacing-filter-btn-v-padding` | 8px | Vertical padding nút filter |
| `--spacing-filter-btn-radius` | 8px | Border radius nút filter |
| `--spacing-filter-btn-gap` | 8px | Gap giữa 2 filter buttons |
| `--spacing-write-btn-padding` | 10px | Padding (all sides) A.1 write button |
| `--spacing-write-btn-radius` | 4px | Border radius A.1 button |
| `--spacing-write-btn-height` | 40px | Height A.1 write button |
| `--spacing-details-card-padding` | 12px | Padding của D.1 stats card và D.3 gift card |
| `--spacing-details-card-radius` | 8px | Border radius D.1/D.3 cards |
| `--spacing-details-card-gap` | 8px | Gap giữa elements trong D.1/D.3 |
| `--spacing-secret-box-btn-height` | 40px | Height D.2 "Mở Secret Box" button |
| `--spacing-secret-box-btn-padding` | 12px | Padding (all sides) D.2 button |
| `--spacing-secret-box-btn-radius` | 4px | Border radius D.2 button |
| `--spacing-stats-row-gap` | 12px | Gap giữa các stat row trong D.1 |
| `--spacing-divider-v-margin` | 12px | Margin vertical trước/sau divider D.1.5 |
| `--spacing-gift-row-gap` | 12.7px | Gap giữa gift recipient rows |
| `--spacing-carousel-peek` | 20px | Phần card bên cạnh visible (peek) trong carousel |
| `--spacing-pagination-text-size` | 13px | Font size của text indicator "N/5" trong B.5.2 |
| `--spacing-pagination-gap` | 4px | Gap giữa nav button và text indicator |

### Border & Radius

| Token Name | Value | Usage |
|------------|-------|-------|
| `--radius-card-highlight` | 8px | B.3 KudosHighlightCard |
| `--radius-card-post` | 12px | C.3 KudosPostCard |
| `--radius-details-card` | 8px | D.1 stats card, D.3 gift card |
| `--radius-filter-btn` | 8px | B.1.1, B.1.2 filter dropdown buttons |
| `--radius-write-btn` | 4px | A.1 write kudos button |
| `--radius-avatar` | 999px | Circular avatar (full radius) |
| `--radius-badge` | 4px | Badge label tag |
| `--radius-hashtag` | 16px | Hashtag chip (pill) |
| `--radius-secret-box-btn` | 4px | D.2 "Mở Secret Box" button |
| `--border-card-highlight` | 1px solid #FFEA9E | B.3 Highlight card border (gold) |
| `--border-card-post` | 1px solid rgba(255,255,255,0.15) | C.3 Post card border |
| `--border-details-card` | 0.8px solid #998C5F | D.1/D.3 details card border (golden brown) |
| `--border-filter-btn` | 1px solid rgba(255,255,255,0.20) | Filter button border |
| `--border-write-btn` | 1px solid #998C5F | A.1 write button border (golden brown) |

### Shadows

| Token Name | Value | Usage |
|------------|-------|-------|
| `--shadow-card` | `0px 4px 16px rgba(0,0,0,0.24)` | KudosHighlightCard (center card) |
| `--shadow-card-side` | None | Cards bên cạnh carousel (không có shadow) |

---

## Layout Specifications

### Screen Container

| Property | Value | Notes |
|----------|-------|-------|
| width | 375px | iPhone standard (375pt) |
| background | `#00101A` | Deep navy |
| overflow | scroll | LazyColumn / ScrollView |

### Layout Structure (ASCII)

```
┌────────────────── 375px ──────────────────┐
│  [HomeHeader — fixed overlay]             │  h:56px, y:0
│  [SAA Logo] [VN▼] [🔍] [🔔 badge]       │
├───────────────────────────────────────────┤
│  A — KV Kudos (keyvisual banner)          │  h:~220px
│  ┌─────────────────────────────────────┐  │
│  │  MM_MEDIA_Keyvisual BG (full-width) │  │
│  │  "Hệ thống ghi nhận và cảm ơn"     │  │  y:~90  14px 400
│  │  KUDOS  (gold, 48px ExtraBold)     │  │  y:~108 gold
│  │  ┌──────────────────────────────┐  │  │
│  │  │ 🖊 Hôm nay, bạn muốn gửi... │  │  │  A.1, y:~158
│  │  └──────────────────────────────┘  │  │
│  └─────────────────────────────────────┘  │
├───────────────────────────────────────────┤
│  B — Highlight Kudos                      │  padH:16px
│  ┌─ B.1 header ──────────────────────┐   │
│  │  "Sun* Annual Awards 2025" 10px   │   │  secondary text
│  │  [Hashtag ▼] [Phòng ban ▼]        │   │  filter row
│  └───────────────────────────────────┘   │
│  ┌─ B.2 Highlight carousel ──────────┐   │
│  │  "HIGHLIGHT KUDOS" 20px Bold gold │   │  section title
│  │  ‹  ┌──────────card──────────┐ ›  │   │  carousel
│  │     │ B.3.1○  B.3.2 text     │    │   │
│  │     │   →   B.3.5○  B.3.2    │    │   │
│  │     │ B.4.1 timestamp        │    │   │
│  │     │ B.4.2 CATEGORY         │    │   │
│  │     │       message text...  │    │   │
│  │     │ B.4.3 #tag1 #tag2      │    │   │
│  │     │ ❤ N  🔗CopyLink ↗Detail│    │   │
│  │     └────────────────────────┘    │   │
│  │  B.5: ‹  "2/5"  ›                │   │  pagination text
│  └───────────────────────────────────┘   │
│  ┌─ B.6 Spotlight header ─────────── ┐   │
│  │  "Sun* Annual Awards 2025"        │   │
│  │  "SPOTLIGHT BOARD" 20px gold      │   │
│  └───────────────────────────────────┘   │
│  ┌─ B.7 Spotlight Board ─────────────┐   │  h:~300px
│  │  B.7.1: "388 KUDOS"              │   │
│  │  B.7.2: Pan/Zoom board           │   │
│  │  B.7.3: [🔍 Tìm kiếm sunner...] │   │
│  └───────────────────────────────────┘   │
├───────────────────────────────────────────┤
│  C — All Kudos                            │  padH:16px
│  ┌─ C.1 header ──────────────────────┐   │
│  │  "Sun* Annual Awards 2025" 10px   │   │
│  │  "ALL KUDOS" 20px Bold gold       │   │
│  └───────────────────────────────────┘   │
│  [C.3 KudosPostCard] ×N (list)           │
│  ┌──────────────────────────────────┐    │
│  │  C.3.1○ sender  → C.3.3○ recv   │    │
│  │  C.3.4 timestamp                 │    │
│  │  C.3.5: CATEGORY                 │    │
│  │         message (5 lines max)    │    │
│  │         #tag1 #tag2              │    │
│  │         ❤ N  🔗CopyLink          │    │
│  └──────────────────────────────────┘    │
│  C.2 "View all Kudos ↗" (text link)      │
├───────────────────────────────────────────┤
│  D — Personal Stats                       │  padH:16px
│  ┌─ D.1 Stats ───────────────────────┐   │
│  │  D.1.2: 25 / Kudos nhận được     │   │
│  │  D.1.3: 25 / Kudos đã gửi        │   │
│  │  D.1.4: 25 / Số tim              │   │
│  │  ────────── divider ──────────── │   │
│  │  D.1.6: 25 / Secret Box đã mở   │   │
│  │  D.1.7: 25 / Secret Box chưa mở │   │
│  └───────────────────────────────────┘   │
│  D.2 [Mở Secret Box 🎁] (CTA button — **INSIDE D.1 card**)  │
│  ┌─ D.3 Gift recipients ─────────────┐   │
│  │  D.3.1: "10 SUNNER NHẬN QUÀ MỚI NHẤT" (gold, center) │
│  │  D.3.2: ○32 Huỳnh Dương Xuân     │   │
│  │         Nhận được 1 áo phông SAA │   │
│  │  D.3.2: ○32 [tên khác]           │   │
│  │         ...                      │   │
│  └───────────────────────────────────┘   │
├───────────────────────────────────────────┤
│  [HomeNavBar — bottom]                    │  h:56px
│  [🏠SAA] [🏆Awards] [💙Kudos*] [👤Profile]│
└───────────────────────────────────────────┘
```

---

## Component Styles

### A.1 — Write Kudos Button

```
background: rgba(255, 234, 158, 0.10)    ← subtle gold tint (NOT white!)
border: 1px solid #998C5F               ← golden brown (NOT white!)
border-radius: 4px                       ← (NOT 8px)
height: 40px
padding: 10px (all sides)
icon: Pen icon (left), size 24×24dp, color: #FFFFFF
text: " Hôm nay, bạn muốn gửi kudos đến ai?   "
      font: Montserrat 14px Medium (500), color: #FFFFFF, textAlign: center
```

### B.1.1 / B.1.2 — Filter Dropdown Buttons

```
background: rgba(255,255,255, 0.08)
border: 1px solid rgba(255,255,255, 0.20)
border-radius: 8px
padding: 8px 12px
icon: ChevronDown (right), size: 16dp, color: #FFFFFF @ 80%
text: font Montserrat 13px Regular, color: #FFFFFF
active state (filter selected):
  background: rgba(255,234,158, 0.12)   ← gold tint
  border: 1px solid rgba(255,234,158, 0.60)
  text/icon color: #FFEA9E
```

### B.3 — KudosHighlightCard (LIGHT CREAM CARD)

```
⚠️ Card có nền sáng (cream), KHÔNG phải nền tối!

background: #FFF8E1                      ← light warm cream
border: 1px solid #FFEA9E               ← gold border
border-radius: 8px                       ← (NOT 12px)
padding: 8px 12px
width: ~273px (50px → 323px within 335px container)
shadow: none on regular state

  Header row ("trao nhận"):
    avatar size: 24×24dp                 ← (NOT 40px!)
    avatar border: 0.865px solid #FFFFFF
    arrow icon between avatars
    sender name: Montserrat 10px Regular (400), color: #00101A (dark on light bg)
    employee code: Montserrat 10px Medium (500), color: #999999 (gray)
    dot separator: 2px circle, #999999 @ 40%
    badge region: below name + code

  Content (B.4):
    timestamp (B.4.1): Montserrat 11px Regular, dark navy
    category title (B.4.2): Montserrat 13px Bold, UPPERCASE, dark navy
    message body (B.4.2): Montserrat 14px Regular, dark navy, max 3 lines
    hashtag text (B.4.3):
      color: #D4271D (red/orange)        ← (NOT white/gold!)
      font: Montserrat 10px Regular (400), lineHeight 11px
      displayed inline (NOT as chips), max 2 lines, overflow truncated

  Action row (B.4.4):
    heart icon: size 18dp
      active: #E73928 (filled)
      inactive: dark navy @ 60% (outline)
    heart count: Montserrat 13px SemiBold, dark navy
    "Copy Link": Montserrat 12px Medium, dark navy @ 60%
    "Xem chi tiết": Montserrat 12px Medium, #00101A or gold link

Side cards (faded):
  opacity: 0.4
  scale: 0.95
  no shadow
```

### B.5 — Pagination (text "N/5" format)

```
// B.5.2 — Số trang (text indicator, NOT dots)
text: "2/5" format (current/total)
font: Montserrat 13px SemiBold, color: white @ 80%

// B.5.1 / B.5.3 — Nav icon buttons
circle 32dp, background: rgba(255,255,255, 0.10)
border: 1px solid rgba(255,255,255, 0.20)
icon: ChevronLeft/Right, size 16dp, white
disabled state: opacity 0.3
```

### C.3 — KudosPostCard (DARK GLASS CARD)

```
background: rgba(255,255,255, 0.06)
border: 1px solid rgba(255,255,255, 0.15)
border-radius: 12px
padding: 16px
margin bottom: 12px

  sender/recipient avatar: 32×32dp, circular
  sender name: Montserrat 13px SemiBold, white
  recipient name: Montserrat 13px SemiBold, white
  star badge: ★ icon (1–3 stars), size 12dp, color: #FFEA9E
  arrow icon: 14dp, white @ 60%
  timestamp: 11px Regular, white @ 60%
  category title: 13px Bold, white, UPPERCASE
  message: 14px Regular, white, max 5 lines
  hashtag chips: pill style, text white @ 60%
  heart + copy link: same as B.4.4 (without "Xem chi tiết")
```

### D.1 — Stats Card (DARK CONTAINER with golden border)

```
⚠️ D.1 và D.2 (secret box button) là CÙNG MỘT container!

container:
  background: #00070C                    ← very dark (NOT transparent/glass)
  border: 0.794px solid #998C5F         ← golden brown border
  border-radius: 8px
  padding: 12px

stat rows layout (5 rows total):
  D.1.2: "Số Kudos bạn nhận được:"     [25]
  D.1.3: "Số Kudos bạn đã gửi:"        [25]
  D.1.4: "Số tim bạn nhận được:"  [🔥x2] [25]
  ──── D.1.5 divider ────
  D.1.6: "Số Secret Box bạn đã mở:"    [25]
  D.1.7: "Số Secret Box chưa mở:"      [25]

  each row:
    layout: flexRow, justifyContent: space-between, alignItems: center
    label: Montserrat 14px Light (300), white, letterSpacing: 0.25px
    value: Montserrat 14px Bold (700), gold (#FFEA9E), right-aligned, letterSpacing: 0.25px
    row gap: ~12px between rows

  D.1.4 hearts row special element (mms_S_Group 435):
    a 🔥 flame multiplier image (24×28.5dp) + "x2" overlay text
    white text Bold 14px, navy stroke 0.82px
    positioned between label and value

  D.1.5 divider:
    height: 1px, color: #2E3940         ← dark gray (NOT white!)
    width: full (stretch)

  D.2 "Mở Secret Box" button (INSIDE D.1 container, after D.1.7):
    background: #FFEA9E                  ← solid gold
    border-radius: 4px                   ← (NOT 8px!)
    height: 40px
    padding: 12px (all sides)
    width: full (stretch to card width)
    label: "Mở Secret Box" — Montserrat 14px Medium (500), #00101A (dark), centered
    icon: gift/box icon (24×24dp) to right of label
```

### D.3 — Gift Recipients Card (DARK CONTAINER with golden border)

```
container: same as D.1
  background: #00070C
  border: 0.794px solid #998C5F
  border-radius: 8px
  padding: 12px

D.3.1 title:
  text: "N SUNNER NHẬN QUÀ MỚI NHẤT" (N = dynamic count)
  font: Montserrat 14px Bold (700), gold (#FFEA9E)
  textAlign: center                      ← (NOT left!)
  width: full

D.3.2 recipient row:
  layout: flexRow, gap 6.35px, alignItems: center, justifyContent: flex-start
  avatar: 32×32dp circular               ← (NOT 44px!)
  avatar border: 1.483px solid #FFFFFF
  gap between rows: 12.7px
  info column: name (14px SemiBold, white) + reward (12px Regular, white@60%)
```

---

## Figma Media Assets

| Asset Name | Node ID | Usage |
|------------|---------|-------|
| `mm_media_bg` | `6885:9060` | Section background overlay |
| `MM_MEDIA_Keyvisual BG` | `6885:9061` | Full KV banner background |
| `mm_media_logo` (SAA logo) | `6885:9071` | Header logo |
| `mm_media_icon` (pen icon) | `I6885:9083;28:2013` | A.1 write button icon |
| `mm_media_IC` (hashtag filter) | `I6885:9088;89:2249` | B.1.1 filter icon |
| `mm_media_IC` (dept filter) | `I6885:9089;89:2249` | B.1.2 filter icon |
| `mm_media_img` (sender avatar) | `I6885:9092;89:2951;89:2714` | B.3.1 avatar |
| `mm_media_danh hiệu` (badge) | `I6885:9092;89:2951;89:2697` | B.3.2 recognition badge |
| `mm_media_IC` (arrow) | `I6885:9092;89:2953` | B.3.4 direction arrow |
| `mm_media_img` (recipient avatar) | `I6885:9092;89:2954;89:2714` | B.3.5 avatar |
| `mm_media_IC` (heart) | `I6885:9092;89:2975` | B.4.4 heart action |
| `mm_media_IC` (copy link) | `I6885:9092;89:3023` | B.4.4 copy action |
| `mm_media_arrow` (detail) | `I6885:9092;89:3030` | B.4.4 detail action |
| `mm_media_next` (next btn) | `6885:9097` | B.5.3 next button |
| `mm_media_next` (prev btn) | `6885:9095` | B.5.1 prev button |
| `mm_media_home` | `I6885:9064;75:2055` | Bottom nav: SAA tab |
| `mm_media_award` | `I6885:9064;75:2058` | Bottom nav: Awards tab |
| `mm_media_kudos` | `I6885:9064;75:2061` | Bottom nav: Kudos tab (active) |
| `mm_media_account` | `I6885:9064;75:2064` | Bottom nav: Profile tab |

---

## Android Implementation Notes

### Composable Structure (recommended)

> Based on actual Figma node tree: `mms_C_All kudos` wraps D.1, D.3 and kudo list.
> For Android implementation, treat each as a separate `item {}` in LazyColumn regardless.

```
KudosScreen
├── HomeHeader (overlay, z-index top)
├── Box (full screen)
│   ├── mm_media_bg (background image, z-index bottom)
│   └── LazyColumn
│       ├── item: KudosKVBanner (section A — frame 6885:9066)
│       ├── item: WriteKudosButton (A.1 — 6885:9083, below banner)
│       ├── item: KudosHighlightSection (B — 6885:9084)
│       │   ├── KudosFilterRow (B.1 — 6885:9085)
│       │   └── KudosHighlightCarousel (B.2 — 6885:9090)
│       │       └── KudosHighlightCard ×5 (B.3)
│       ├── item: Box (B.2.1 ‹ / B.2.2 › overlay on carousel — root 6885:9094 + 9096)
│       ├── item: KudosPagination (B.5 — 6885:9098, text "N/5")
│       ├── item: SpotlightBoardSection (B.6 — 6885:9099)
│       │   └── SpotlightBoard (B.7 — 6885:9101)
│       ├── item: AllKudosSectionHeader (C.1 — 6885:9221)
│       ├── item: PersonalStatsGrid (D.1 — 6885:9223)
│       ├── item: OpenSecretBoxButton (D.2)
│       ├── item: GiftRecipientsList (D.3 — 6885:9255)
│       └── items: KudosPostCard ×N (Danh sách Kudo — 6891:15986)
│           + item: ViewAllKudosLink (C.2, "View all Kudos ↗")
└── HomeNavBar (overlay, z-index bottom)

// Sticky A.1 (6891:21267): dùng stickyHeader {} hoặc FloatingActionButton
// overlay khi user scroll qua banner A
```

### Key Android-specific Notes

- **Carousel B.3**: dùng `HorizontalPager` (Accompanist/Compose) với `pageSize = Fixed(311.dp)` và `beyondBoundsPageCount = 1`
- **LazyColumn nested**: dùng `items` cho C.3 list — tránh nested scroll conflict bằng `nestedScroll`
- **Spotlight Board B.7**: custom Canvas composable với `detectTransformGestures` (pan + zoom)
- **Realtime**: Supabase Realtime trong ViewModel, `StateFlow<List<Kudos>>` update qua `MutableStateFlow`
- **Heart toggle**: optimistic UI update trước khi API response
- **Image loading**: `coil.compose.AsyncImage` cho tất cả avatar
- **Background blur** (nếu có): dùng `BlurMaskFilter` hoặc library `haze`
- **Color palette đã có**: `Background = Color(0xFF00101A)`, `ButtonPrimaryBg = Color(0xFFFFEA9E)`, `TextOnDark = Color(0xFFFFFFFF)`
