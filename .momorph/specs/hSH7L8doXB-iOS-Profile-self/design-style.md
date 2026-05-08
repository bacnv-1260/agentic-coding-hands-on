# Design Style: [iOS] Profile bản thân

**Frame ID**: `6885:10333`
**Screen ID**: `hSH7L8doXB`
**Frame Name**: `[iOS] Profile bản thân`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/hSH7L8doXB
**Extracted At**: 2026-05-06

---

## Design Tokens

### Colors

| Token Name | Hex Value | Opacity | Usage |
|------------|-----------|---------|-------|
| `--color-background` | `#00101A` | 100% | Screen background (toàn màn hình, bên dưới keyvisual) |
| `--color-keyvisual-shadow-left` | `#00101A → #10181F → transparent` | — | Gradient shadow overlay trái keyvisual bg |
| `--color-profile-name` | `#FFEA9E` | 100% | Tên người dùng (A.2.1) — gold |
| `--color-profile-info` | `#FFFFFF` | 100% | Mã nhân viên, icon collection label, stats labels |
| `--color-stats-value` | `#FFEA9E` | 100% | Giá trị số trong stats rows — gold, right-aligned |
| `--color-stats-container-bg` | `#00070C` | 100% | Background D.1 Stats Container (`var(--Details-Container-2)`) |
| `--color-stats-container-border` | `#998C5F` | 100% | Border 0.794px D.1 Stats Container (`var(--Details-Border)`) |
| `--color-divider` | `#2E3940` | 100% | Divider D.1.5 giữa Kudos/Tim và Secret Box stats |
| `--color-button-primary-bg` | `#FFEA9E` | 100% | "Mở Secret Box" button background — gold |
| `--color-button-primary-text` | `#00101A` | 100% | Label trên primary button (dark on gold) |
| `--color-filter-pill-bg` | `#FFEA9E` | 10% | Filter dropdown pill background (`rgba(255,234,158,0.10)`) |
| `--color-filter-pill-border` | `#998C5F` | 100% | Filter dropdown pill border (`var(--Details-Border)`) |
| `--color-dropdown-overlay-bg` | `#00070C` | 100% | Dropdown overlay background (`var(--Details-Container-2)`) |
| `--color-dropdown-overlay-border` | `#998C5F` | 100% | Dropdown overlay border |
| `--color-dropdown-option-text` | `#FFFFFF` | 100% | Option text trong dropdown overlay |
| `--color-dropdown-option-glow` | `#FAE287` | 100% | text-shadow glow của active dropdown option |
| `--color-section-title` | `#FFEA9E` | 100% | "KUDOS" section title — gold |
| `--color-section-subtitle` | `#FFFFFF` | 100% | "Sun* Annual Awards 2025" sub-label |
| `--color-card-highlight-bg` | `#FFF8E1` | 100% | KudosHighlightCard background (light warm yellow) |
| `--color-card-highlight-border` | `#FFEA9E` | 100% | KudosHighlightCard border 1px — gold |
| `--color-card-text-on-light` | `#00101A` | 100% | Text trên KudosHighlightCard (name, content, category) |
| `--color-card-team-code` | `#999999` | 100% | Mã nhân viên trong Kudos card header (gray) |
| `--color-card-hashtag` | `#D4271D` | 100% | ⚠️ Hashtag text trong KudosHighlightCard — ĐỎ (không phải gold) |
| `--color-notification-badge` | `#D4271D` | 100% | Bell icon unread dot |
| `--color-badge-legend-bg` | `#E73928` | 100% | Badge "Legend Hero" background tint |
| `--color-nav-active` | `#FFEA9E` | 100% | Active tab icon trong bottom nav |
| `--color-nav-inactive` | `#FFFFFF` | 40% | Inactive tab icon trong bottom nav |

> ⚠️ **`--color-card-hashtag` = `#D4271D` (đỏ)** — không phải gold. Shared token với KudosHighlightCard trên Kudos screen (`fO0Kt19sZZ` `--color-hashtag-highlight`).

---

### Typography

| Token Name | Font Family | Size | Weight | Line Height | Letter Spacing | Usage |
|------------|-------------|------|--------|-------------|----------------|-------|
| `--text-profile-name` | Montserrat | 18px | 700 (Bold) | 24px | 0px | Tên đầy đủ người dùng (A.2.1) — gold |
| `--text-profile-info` | Montserrat | 14px | 400 (Regular) | 20px | 0.25px | Mã nhân viên (A.2.2) — white |
| `--text-badge` | Montserrat | ~8px | 700 (Bold) | ~10px | 0.06px | Badge tier label (e.g. "Legend Hero") — white, text-shadow glow |
| `--text-collection-label` | Montserrat | 12px | 400 (Regular) | 16px | 0px | "Bộ sưu tập icon của tôi" (A.3) — white, centered |
| `--text-stats-label` | Montserrat | 14px | 300 (Light) | 20px | 0.25px | Stats row labels — white, right-aligned |
| `--text-stats-value` | Montserrat | 14px | 700 (Bold) | 20px | 0.25px | Stats numbers — gold `#FFEA9E`, right-aligned |
| `--text-secret-box-btn` | Montserrat | 14px | 500 (Medium) | 20px | 0px | Label "Mở Secret Box" — dark `#00101A` |
| `--text-section-subtitle` | Montserrat | 12px | 400 (Regular) | 16px | 0px | "Sun* Annual Awards 2025" sub-label — white |
| `--text-section-title` | Montserrat | 22px | 500 (Medium) | 28px | 0px | "KUDOS" section title — gold `#FFEA9E` |
| `--text-filter-pill` | Montserrat | 14px | 400 (Regular) | 20px | 0.25px | Filter dropdown pill text — white |
| `--text-dropdown-option` | Montserrat | 14px | 500 (Medium) | 20px | 0.10px | Dropdown overlay option — white (active: glow `#FAE287`) |
| `--text-card-name` | Montserrat | 10px | 400 (Regular) | 16px | 0px | Sender/recipient name trong Kudos card — dark `#00101A` |
| `--text-card-team-code` | Montserrat | 10px | 500 (Medium) | ~9.3px | 0.046px | Employee code trong Kudos card — gray `#999999` |
| `--text-card-timestamp` | Montserrat | 10px | 400 (Regular) | 16px | 0px | Timestamp trong Kudos card |
| `--text-card-category` | Montserrat | 14px | 700 (Bold) | 20px | 0.3px | Award category label — dark `#00101A` |
| `--text-card-message` | Montserrat | 14px | 400 (Regular) | 20px | 0px | Nội dung lời cảm ơn — dark `#00101A` |
| `--text-card-hashtag` | Montserrat | 10px | 400 (Regular) | 11px | 0.23px | Hashtag — **đỏ `#D4271D`** |
| `--text-card-action` | Montserrat | 12px | 500 (Medium) | 16px | 0px | "Copy Link", "Xem chi tiết" — dark `#00101A` |
| `--text-language-label` | Montserrat | 14px | 500 (Medium) | 20px | 0px | "VN" language label trong header — white |

---

### Spacing

| Token Name | Value | Usage |
|------------|-------|-------|
| `--spacing-screen-h-padding` | 20px | Horizontal margin content sections (20px each side) |
| `--spacing-profile-section-gap` | 24px | Gap giữa avatar và name frame trong `mms_1.1_member` |
| `--spacing-name-frame-gap` | 4px | Gap giữa name text và detail row trong `mms_A.2_Name` |
| `--spacing-collection-gap` | 12px | Gap giữa icon row và label trong icon collection |
| `--spacing-stats-container-padding` | 12px | Padding tất cả 4 cạnh D.1 Stats Container |
| `--spacing-stats-container-gap` | 8px | Gap giữa các rows trong Stats Container |
| `--spacing-secret-box-btn-padding` | 12px | Horizontal padding "Mở Secret Box" button |
| `--spacing-secret-box-btn-height` | 40px | Height của "Mở Secret Box" button |
| `--spacing-secret-box-btn-gap` | 8px | Gap icon ↔ label trong button |
| `--spacing-filter-pill-padding` | 8px | Padding tất cả 4 cạnh của filter dropdown pill |
| `--spacing-filter-pill-height` | 40px | Height của filter dropdown pill |
| `--spacing-filter-pill-gap` | 8px | Gap text ↔ caret icon trong pill |
| `--spacing-dropdown-overlay-padding` | 6px | Padding dropdown overlay container |
| `--spacing-kudos-card-padding-h` | 12px | Horizontal padding KudosHighlightCard |
| `--spacing-kudos-card-padding-v` | 8px | Vertical padding KudosHighlightCard |
| `--spacing-kudos-card-gap` | 8px | Gap giữa các elements trong card |
| `--spacing-kudos-list-gap` | 24px | Gap giữa các card trong danh sách |
| `--spacing-avatar-size` | 72px | Profile avatar (main, circular) |
| `--spacing-avatar-size-card` | 24px | Avatar trong Kudos card header |
| `--spacing-badge-icon-size` | ~36px | Icon badge slot trong collection row |

---

### Border & Radius

| Token Name | Value | Usage |
|------------|-------|-------|
| `--radius-stats-container` | 8px | D.1 Stats Container |
| `--radius-secret-box-btn` | 4px | "Mở Secret Box" button |
| `--radius-filter-pill` | 4px | Filter dropdown pill |
| `--radius-dropdown-overlay` | 8px | Dropdown overlay container |
| `--radius-card-highlight` | 8px | KudosHighlightCard |
| `--radius-avatar` | 999px | Circular avatar (tất cả kích cỡ) |
| `--radius-badge-icon` | 999px | Icon badge slot (circular) |
| `--border-stats-container` | `0.794px solid #998C5F` | D.1 Stats Container (exact from Figma) |
| `--border-filter-pill` | `1px solid #998C5F` | Filter dropdown pill |
| `--border-dropdown-overlay` | `1px solid #998C5F` | Dropdown overlay |
| `--border-card-highlight` | `1px solid #FFEA9E` | KudosHighlightCard — gold |

---

### Shadows

| Token Name | Value | Usage |
|------------|-------|-------|
| `--shadow-none` | none | Hầu hết elements không có shadow trên dark bg |
| `--shadow-dropdown-option` | `0 4px 4px rgba(0,0,0,0.25), 0 0 6px #FAE287` | text-shadow của active dropdown option |
| `--shadow-badge-text` | `0 0 0.8px #FFF` | text-shadow của badge tier label text |

---

## Layout Specifications

### Screen Container

| Property | Value | Notes |
|----------|-------|-------|
| Width | 375dp | iPhone SE / standard |
| Total scroll height | ~1841dp | Scrollable content |
| Viewport height | 812dp | iPhone standard |
| Horizontal padding | 20dp each side | Content sections margin |

### Layout Structure (ASCII)

```
375dp
┌──────────────────────────────────────────┐
│ HEADER [0–104dp] — position: fixed       │
│ ┌─────────────────────────────────────┐  │
│ │ [Logo SAA 2025]    [VN▼][🔍][🔔]  │  │
│ └─────────────────────────────────────┘  │
│  bg-overlay: #00101A → transparent       │
├──────────────────────────────────────────┤
│ KEYVISUAL BG group [0–~380dp]            │
│ (MM_MEDIA_Keyvisual BG + Shadow L/B)     │
├──────────────────────────────────────────┤
│ PROFILE INFO [144–288dp]                 │
│ ┌──────────[237dp, center]────────────┐  │
│ │        ⬤ Avatar 72×72dp            │  │
│ │         gap: 24dp                   │  │
│ │  "Huỳnh Dương Xuân Nhật"            │  │
│ │   18sp Bold  color:#FFEA9E          │  │
│ │  [CEVC3   •   Legend Hero ]         │  │
│ │   14sp/white   ~8sp/Bold/white      │  │
│ └─────────────────────────────────────┘  │
│                                          │
│ ICON COLLECTION [312–372dp]              │
│ ┌──────────[262dp, center]────────────┐  │
│ │  [🔵][🔵][🔵][🔵][🔵][🔵]          │  │
│ │  "Bộ sưu tập icon của tôi"          │  │
│ │   12sp Regular  white  centered     │  │
│ └─────────────────────────────────────┘  │
│                                          │
│ STATS CONTAINER [396–632dp]              │
│ ┌──────────[336dp]────────────────────┐  │
│ │ bg:#00070C  border:0.794px #998C5F  │  │
│ │ radius:8dp  padding:12dp  gap:8dp   │  │
│ │                                     │  │
│ │  Số Kudos bạn nhận được:      5    │  │
│ │  Số Kudos bạn đã gửi:        25    │  │
│ │  Số tim bạn nhận được:       25    │  │
│ │  ─── divider  #2E3940 ─────────    │  │
│ │  Số Secret Box bạn đã mở:    25    │  │
│ │  Số Secret Box chưa mở:      25    │  │
│ │                                     │  │
│ │  ┌── 🎁 Mở Secret Box ──────────┐  │  │
│ │  │  bg:#FFEA9E  h:40dp  r:4dp   │  │  │
│ │  └──────────────────────────────┘  │  │
│ └─────────────────────────────────────┘  │
│                                          │
│ KUDOS SECTION HEADER [672–725dp]         │
│  "Sun* Annual Awards 2025"  12sp white   │
│  ─────────────────────────────           │
│  "KUDOS"  22sp Medium  #FFEA9E           │
│                                          │
│ FILTER DROPDOWN PILL [741–781dp]         │
│  ┌─────────────────┐                     │
│  │ Đã gửi (5)  ▾  │ bg:rgba(FFEA9E,.10) │
│  │  border:#998C5F  h:40dp  r:4dp       │
│  └─────────────────┘                     │
│  [when open] OVERLAY [788–880dp]         │
│  ┌───────[118dp]───────────────┐         │
│  │ bg:#00070C  border:#998C5F  │         │
│  │ radius:8dp  padding:6dp     │         │
│  │  [ Đã nhận (5) ]            │         │
│  │  [ Đã gửi (5)  ]            │         │
│  └─────────────────────────────┘         │
│                                          │
│ KUDOS LIST [805–1745dp]  gap:24dp        │
│ ┌────────────────────────────────────┐   │
│ │ KudosHighlightCard                 │   │
│ │ bg:#FFF8E1  border:1px #FFEA9E    │   │
│ │ radius:8dp  padding:8dp 12dp      │   │
│ │ ┌──⬤──→──⬤──┐                    │   │
│ │ │[Name][Code][Badge] ×2           │   │
│ │ └────────────┘                     │   │
│ │ [HH:mm - dd/MM/yyyy]               │   │
│ │ [Category  Bold 14sp  #00101A]     │   │
│ │ [Content message text...]          │   │
│ │ [#tag1 #tag2]  ← #D4271D RED ⚠️   │   │
│ │ [❤ 1.000] [Copy Link] [Xem →]     │   │
│ └────────────────────────────────────┘   │
│  (×N cards repeat)                       │
│                                          │
│ BOTTOM NAV [1769–1841dp] — fixed         │
│  bg:#001828  height:72dp                 │
│  [SAA 2025][Awards][Kudos][Profile ★]    │
└──────────────────────────────────────────┘
```

---

## Component Style Details

### Profile Avatar (Node `6885:10340`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10340` |
| Shape | Circle |
| Size | 72×72dp |
| Content | Coil `AsyncImage`, `ContentScale.Crop` |
| Border | None |
| Fallback | Circular placeholder |

**States:**

| State | Visual |
|-------|--------|
| Loading | Circular shimmer |
| Loaded | Profile photo |
| Error | Default avatar placeholder |

---

### Stats Container (Node `6885:10358`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10358` |
| Width | 336dp |
| Height | 236dp |
| Background | `#00070C` |
| Border | `0.794px solid #998C5F` |
| Border-radius | 8dp |
| Padding | 12dp (all sides) |
| Gap | 8dp |

**States:**

| State | Visual |
|-------|--------|
| Loading | Shimmer skeleton 5 rows + button |
| Loaded | Stats values populated |
| Error | Error message + Retry |

---

### "Mở Secret Box" Button (Node `6885:10386`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10386` |
| Width | 312dp |
| Height | 40dp |
| Background (enabled) | `#FFEA9E` |
| Background (disabled) | `rgba(255,234,158,0.4)` |
| Border-radius | 4dp |
| Padding | 12dp horizontal |
| Layout | Row center, gap 8dp |
| Label | "Mở Secret Box" + 🎁 icon |
| Label size | Montserrat Medium 500 14sp |
| Label color (enabled) | `#00101A` |
| Label color (disabled) | `rgba(0,16,26,0.4)` |

**States:**

| State | Condition | Visual |
|-------|-----------|--------|
| Enabled | `secret_boxes_unopened > 0` | Gold bg, dark text |
| Disabled | `secret_boxes_unopened == 0` | Faded gold, faded text, no tap |

---

### Filter Dropdown Pill (Node `6885:10388`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10388` |
| Height | 40dp |
| Background | `rgba(255,234,158,0.10)` |
| Border | `1dp solid #998C5F` |
| Border-radius | 4dp |
| Padding | 8dp all |
| Text | Montserrat Regular 400 14sp white |
| Icon | Caret-down, white |

**States:**

| State | Visual |
|-------|--------|
| Closed | Shows active filter text + caret |
| Open | Dropdown overlay visible below |

---

### KudosHighlightCard (Nodes `6885:10390`–`6885:10392`)

Tái sử dụng composable tại `presentation/ui/kudos/components/KudosHighlightCard.kt`.

| Property | Value |
|----------|-------|
| Background | `#FFF8E1` |
| Border | `1px solid #FFEA9E` |
| Border-radius | 8dp |
| Padding | 8dp v / 12dp h |
| Width | screen-width − 2×20dp |

**Card header sub-elements:**

| Element | Property | Value |
|---------|----------|-------|
| Sender avatar | Size | 24×24dp circle |
| Arrow icon | Color | white |
| Recipient avatar | Size | 24×24dp circle |
| Sender name | Style | Montserrat Regular 400 10sp `#00101A` |
| Team code | Style | Montserrat Medium 500 10sp `#999999` |
| Badge | Style | `mm_media_danh hiệu` mini pill |

**Card body sub-elements:**

| Element | Property | Value |
|---------|----------|-------|
| Timestamp | Style | 10sp Regular, format `HH:mm - dd/MM/yyyy` |
| Award category | Style | Montserrat Bold 700 14sp `#00101A` |
| Content text | Style | Montserrat Regular 400 14sp `#00101A`, truncated |
| Photos | Layout | 2×2 grid, radius 4dp |
| **Hashtags** | **Color** | **`#D4271D` (RED) ⚠️ — NOT gold** |
| Hashtags | Style | Montserrat Regular 400 10sp `#D4271D` |

**Card footer:**

| Element | Style |
|---------|-------|
| Heart count ❤️ | Montserrat SemiBold 600 13sp `#00101A` |
| Copy Link | Text btn, Montserrat Medium 500 12sp `#00101A` |
| Xem chi tiết ↗ | Text btn, Montserrat Medium 500 12sp `#00101A` |

---

## Reused Components

| Component | File | Notes |
|-----------|------|-------|
| `HomeHeader` | `presentation/ui/home/components/HomeHeader.kt` | Header chung — không cần back button |
| `HomeNavBar` | `presentation/ui/home/components/HomeNavBar.kt` | Bottom nav — Profile tab (index 3) active |
| `KudosHighlightCard` | `presentation/ui/kudos/components/KudosHighlightCard.kt` | Card tái dụng — **hashtag `#D4271D`** |

