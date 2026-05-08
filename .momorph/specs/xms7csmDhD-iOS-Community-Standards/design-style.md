# Design Style: [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng

**Frame ID**: `TBD` *(⚠️ Figma export rate-limited at spec creation time — verify node ID before implementation)*
**Screen ID**: `xms7csmDhD`
**Frame Name**: `[iOS] Sun*Kudos_Tiêu chuẩn cộng đồng`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/xms7csmDhD
**Extracted At**: 2026-05-08

> ⚠️ **Note**: Design tokens in this file are derived from the shared Kudos design system (same file key `9ypp4enmFmdK3YAFJLIu6C`). Values for colors, typography and spacing are inferred from the Kudos dark theme used across `fO0Kt19sZZ`, `7fFAb-K35a` and `k-7zJk2B7s`. Verify each token against the actual Figma frame before shipping.

---

## 1. Screen Layout

| Property | Value |
|----------|-------|
| Orientation | Portrait only |
| Background | `#00101A` (Kudos dark background token) |
| Safe area | Respects system status bar (top) via `Scaffold`; bottom safe area inset for scroll end padding |
| Scroll | Full vertical scroll via `Column(modifier = Modifier.verticalScroll(rememberScrollState()))` — header is fixed |
| Layout structure | `Column`: fixed Header → scrollable Content area |

---

## 2. Color Tokens

All colors are from the shared `SaaDarkColorScheme` and Kudos design tokens. No new colors are expected for this screen.

| Role | Token / Value | Usage |
|------|--------------|-------|
| Screen background | `--color-background = #00101A` | `Scaffold` background |
| Header title text | `--color-text-gold = #FFEA9E` | Title "Tiêu chuẩn cộng đồng" |
| Back arrow icon | `--color-text-on-dark = #FFFFFF` | `IconButton` tint |
| Header divider | `--color-divider-dark = #2E3940` | `HorizontalDivider` under header title |
| Section title text | `--color-text-gold = #FFEA9E` | Each section heading |
| Section body text | `--color-text-on-dark = #FFFFFF` | Body / paragraph text (100% opacity) |
| Section body text (secondary) | `--color-text-secondary = rgba(255,255,255,0.60)` | Supporting/hint text within sections |
| Section divider | `--color-divider-dark = #2E3940` | Horizontal rule between sections |
| Bullet / list marker | `--color-text-gold = #FFEA9E` | Bullet points or numbered list markers |
| Prohibited content highlight | `--color-heart-active = #E73928` | Red text for prohibited examples |
| Community link text | `#E73928` per Figma design token — ⚠️ **Code deviation**: `FormattingToolbar.kt` currently uses `Color(0xFFE46060)` (lighter pink-red). Correct to `#E73928` when implementing CommunityStandardsScreen; also update `FormattingToolbar.kt` to use the correct token. | "Tiêu chuẩn cộng đồng" link text in toolbar |

> Inherited color tokens (defined in `fO0Kt19sZZ-iOS-Sun-Kudos/design-style.md`):
> - `--color-background`: `#00101A`
> - `--color-text-gold`: `#FFEA9E`
> - `--color-text-on-dark`: `#FFFFFF`
> - `--color-text-secondary`: `#FFFFFF` at 60% opacity
> - `--color-divider-dark`: `#2E3940`
> - `--color-heart-active` / red accent: `#E73928`

---

## 3. Typography

| Element | Style | Font | Weight | Size |
|---------|-------|------|--------|------|
| Screen title "Tiêu chuẩn cộng đồng" | `MaterialTheme.typography.titleLarge` | Montserrat | **Bold** (`FontWeight.Bold`) | **22sp** (Material3 `titleLarge` baseline; consistent with other detail-screen headers e.g. Access Denied) |
| Section title (e.g., "Kudos là gì?") | `MaterialTheme.typography.titleMedium` | Montserrat | SemiBold (`FontWeight.SemiBold`) | ~16sp, color `#FFEA9E` |
| Section body text | `MaterialTheme.typography.bodyMedium` | Montserrat / default | Regular (`FontWeight.Normal`) | ~14sp, color `#FFFFFF` |
| Bullet list item | `MaterialTheme.typography.bodyMedium` | same as body | Regular | ~14sp |
| Prohibited example text | `MaterialTheme.typography.bodySmall` | same | Regular | ~13sp, color `#E73928` |
| Example / emphasis text | `MaterialTheme.typography.bodySmall` | same | Italic / Regular | ~13sp, color `rgba(255,255,255,0.60)` |

> Typography tokens `titleLarge`, `titleMedium`, `bodyMedium`, `bodySmall` are defined in `ui/theme/Type.kt` using Montserrat (Google Fonts).

---

## 4. Spacing

| Token Name | Value | Usage |
|------------|-------|-------|
| `--spacing-screen-h-padding` | 20px | Horizontal padding of scrollable content area |
| `--spacing-header-v-padding` | 12px | Vertical padding inside header (above/below title) |
| `--spacing-section-gap` | 24px | Vertical gap between sections |
| `--spacing-section-title-body-gap` | 8px | Gap between section title and its body text |
| `--spacing-section-internal-gap` | 6px | Gap between paragraphs / list items within a section |
| `--spacing-divider-v-margin` | 16px | Vertical margin above and below section dividers |
| `--spacing-bottom-padding` | 24px | Extra bottom padding at end of scroll content — no bottom nav bar on this screen, value accounts for system gesture area / safe area inset |

---

## 5. Border & Radius

| Token | Value | Usage |
|-------|-------|-------|
| `--border-header-divider` | `1px solid #2E3940` | `HorizontalDivider` under header |
| `--border-section-divider` | `1px solid #2E3940` | Divider between content sections |
| No card containers used | — | Content is flat text; no card borders |

---

## 6. Components

### 6.1 mms_Header

| Sub-element | Property | Value |
|-------------|------------|-------|
| `back_arrow` | Component | `IconButton` + `Icons.AutoMirrored.Filled.ArrowBack` |
| `back_arrow` | Position | Top-left, `padding(start = 4.dp, top = 4.dp)` |
| `back_arrow` | Icon tint | `MaterialTheme.colorScheme.onSurface` (`#FFFFFF`) |
| `back_arrow` | Accessibility | `semantics { contentDescription = stringResource(R.string.cd_back_arrow) }` |
| Title | Text | `stringResource(R.string.community_standards_title)` |
| Title | Color | `#FFEA9E` (gold) |
| Title | Alignment | `TextAlign.Center` |
| Title | Weight | `FontWeight.Bold` |
| Title | Style | `MaterialTheme.typography.titleLarge` |
| Title | Padding | `horizontal = 16.dp, vertical = 8.dp` |
| `TopAppBar` containerColor | `#00101A` | No keyvisual on this screen — use solid dark bg (unlike WriteKudo which uses Transparent over art) |
| `TopAppBar` scrolledContainerColor | `#00101A` | Same as containerColor (no scroll-triggered change needed) |
| Divider | Component | `HorizontalDivider` |
| Divider | Color | `MaterialTheme.colorScheme.outlineVariant` (`#2E3940`) |

---

### 6.2 SectionBlock (repeated per section)

| Sub-element | Property | Value |
|-------------|----------|-------|
| Section title | Style | `MaterialTheme.typography.titleMedium` |
| Section title | Color | `#FFEA9E` |
| Section title | Weight | `FontWeight.SemiBold` |
| Section body | Style | `MaterialTheme.typography.bodyMedium` |
| Section body | Color | `#FFFFFF` (onSurface) |
| Section body | Line height | ~20sp |
| Bullet list marker | Color | `#FFEA9E` (gold dot or dash) |
| Prohibited text | Color | `#E73928` (red) |
| Section divider | Component | `HorizontalDivider` (between sections) |
| Section divider | Color | `#2E3940` |
| Section divider | Margin | `vertical = 16.dp` |

---

## 7. Layout Specifications

### Screen Container

| Property | Value | Notes |
|----------|-------|-------|
| Width | 375px | iPhone standard (375pt) |
| Background | `#00101A` | Full screen dark background |
| Content | Fixed header + `Column(modifier = Modifier.verticalScroll(rememberScrollState()))` | Use `Column+verticalScroll` (static sections, fixed count) — **not** `LazyColumn` (reserved for dynamic lists) |
| Bottom nav bar | **None** | This screen has no `BottomNavBar` — same pattern as WriteKudo |

### Layout Structure (ASCII)

```
┌────────────────── 375px ──────────────────┐
│  Status Bar (system)                      │
├───────────────────────────────────────────┤
│  Header (fixed, h≈52dp)                   │
│  ← back   "Tiêu chuẩn cộng đồng"         │  gold title, centered
│  ─────────────── Divider ─────────────── │  #2E3940
├───────────────────────────────────────────┤
│  Column + verticalScroll                  │
│  padding: h=20dp, top=16dp, bottom=24dp   │
│                                           │
│  ┌─────────────────────────────────────┐  │
│  │  Section Title 1 (gold, SemiBold)   │  │  e.g., "Kudos là gì?"
│  │  Body text (white, Regular, 14sp)   │  │
│  │  …                                  │  │
│  └─────────────────────────────────────┘  │
│  ──── Divider (#2E3940) ──── 16dp margin  │
│  ┌─────────────────────────────────────┐  │
│  │  Section Title 2 (gold, SemiBold)   │  │  e.g., "Cách đặt danh hiệu"
│  │  Body text + example list           │  │
│  │    • Example 1 (italic / gray)      │  │
│  │    • Example 2                      │  │
│  └─────────────────────────────────────┘  │
│  ──── Divider ──────────────────────────  │
│  ┌─────────────────────────────────────┐  │
│  │  Section Title 3 (gold)             │  │  e.g., "Hashtag hợp lệ"
│  │  Body text                          │  │
│  └─────────────────────────────────────┘  │
│  ──── Divider ──────────────────────────  │
│  ┌─────────────────────────────────────┐  │
│  │  Section Title 4 (gold)             │  │  e.g., "Nội dung bị cấm"
│  │  • Item 1 (red accent)              │  │
│  │  • Item 2                           │  │
│  └─────────────────────────────────────┘  │
│  ──── Divider ──────────────────────────  │
│  ┌─────────────────────────────────────┐  │
│  │  Section Title 5 (gold)             │  │  e.g., "Ghi nhận ẩn danh"
│  │  Body text                          │  │
│  └─────────────────────────────────────┘  │
│                                           │
│  [bottom padding = 24dp]                  │
└───────────────────────────────────────────┘
```

---

## 8. Component States

### Back Arrow

| State | Icon Color |
|-------|------------|
| Default | `#FFFFFF` (onSurface) |
| Pressed | Handled by Material3 `IconButton` ripple automatically — no custom override needed |

### Scroll Content

| State | Behavior |
|-------|----------|
| At top | No shadow on header |
| Scrolled down | Optional: subtle shadow under header divider (system default) |

---

## 9. String Resources Required

> Key naming convention: `community_standards_s{N}_{title|body}` — use `sN` prefix consistently (matches design-style keys; **not** `section_N` which was used in an earlier draft of spec.md — that draft has been corrected).

| Key | VN Value (example) | EN Value (example) |
|-----|-------------------|-------------------|
| `community_standards_title` | Tiêu chuẩn cộng đồng | Community Standards |
| `community_standards_s1_title` | Kudos là gì? | What is a Kudo? |
| `community_standards_s1_body` | Kudos là lời ghi nhận và cảm ơn... | A Kudo is a recognition message... |
| `community_standards_s2_title` | Cách đặt danh hiệu | How to write a title |
| `community_standards_s2_body` | Danh hiệu nên ngắn gọn, cụ thể... | The title should be concise... |
| `community_standards_s3_title` | Hashtag hợp lệ | Valid hashtags |
| `community_standards_s3_body` | Sử dụng tối đa 5 hashtag liên quan... | Use up to 5 relevant hashtags... |
| `community_standards_s4_title` | Nội dung bị cấm | Prohibited content |
| `community_standards_s4_body` | Không đăng nội dung xúc phạm, kỳ thị... | Do not post offensive, discriminatory... |
| `community_standards_s5_title` | Ghi nhận ẩn danh | Anonymous recognition |
| `community_standards_s5_body` | Bạn có thể chọn gửi ẩn danh... | You may choose to send anonymously... |
| `cd_back_arrow` | Quay lại | Go back | ✅ **Already exists** in `values/strings.xml` L18 and `values-en/strings.xml` L18 — no action needed |

> ⚠️ Exact content strings and actual section count must be verified against the Figma frame text layers before implementation. All values above are placeholders.
