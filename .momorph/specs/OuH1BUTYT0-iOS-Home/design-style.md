# Design Style: [iOS] Home

**Frame**: `OuH1BUTYT0`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Frame Node**: `6885:8978`
**Screen Size**: 375×812dp (iPhone, scrollable to ≈1942dp)
**Created**: 2026-05-05

---

## 1. Screen Background

The Home screen has a **full-bleed keyvisual background** (Node 6885:8979 `mm_media_bg`) composed of 3 layers stacked:

| Layer | Node | Type | Style |
|-------|------|------|-------|
| Keyvisual BG | 6885:8980 | RECTANGLE image | `mm_media_keyvisual_bg.png`, fill screen, `ContentScale.Crop`, align CenterEnd |
| Shadow Left | 6885:8981 | RECTANGLE | `linear-gradient(90deg, #00101A 0.07%, #10181F 18.61%, transparent 77.2%)` |
| Shadow Bottom | 6885:8982 | RECTANGLE | `linear-gradient(90deg, #00101A 0%, #00101A 25.41%, rgba(0,16,26,0.00) 100%)` — node is rotated `−90deg` in Figma, so in Compose this is a **vertical** gradient applied at the bottom of the keyvisual (dark at bottom, transparent at top) |

The main content sections sit **on top of** this background without an additional background fill.

---

## 2. Color Tokens

All existing `SaaDarkColorScheme` tokens apply. No new top-level colors needed.

| Token | Value | Usage |
|-------|-------|-------|
| `Background` | `#00101A` | Screen bg (for areas without keyvisual), header gradient start |
| `ButtonPrimaryBg` | `#FFEA9E` | Section title text, button backgrounds, FAB background, nav bar tint base |
| `TextOnButton` | `#00101A` | Button label text, FAB separator "/" text |
| `TextOnDark` / `onSurface` | `#FFFFFF` | Body text, countdown labels, description paragraphs |
| `DividerLine` / `outlineVariant` | `#2E3940` | Section header dividers (Rectangle 26), countdown separator |
| Notification badge | `#D4271D` | Bell icon unread dot (8×8dp, radius 100dp) |
| Kudos banner bg | `#0F0F0F` | Sun\*Kudos banner background (mms_5.2) |
| Nav bar tint | `rgba(255, 234, 158, 0.15)` | Bottom nav bar semi-transparent background |
| FAB glow | `#FAE287` | FAB box-shadow outer glow |

---

## 3. Typography Tokens

All use **Montserrat** (Google Fonts) except system status bar.

| Element | FontFamily | Weight | Size | LineHeight | LetterSpacing | Color |
|---------|-----------|--------|------|------------|---------------|-------|
| "Coming soon" | Montserrat | Light 300 | 14sp | 20sp | 0.25px | white |
| Countdown number | Montserrat | Bold 700 | ~40sp *(TBD — verify from Figma node 6885:8988)* | — | — | white |
| Countdown label (DAYS/HOURS/MINUTES) | Montserrat | Regular 400 | ~12sp *(TBD — verify from Figma node 6885:8988)* | — | — | white |
| Event info (date/venue/livestream) | Montserrat | Light 300 | 14sp | 20sp | 0.25px | white |
| Section event label ("Sun\* Annual Awards 2025") | Montserrat | Regular 400 | 12sp | 16sp | 0% | white |
| Section title ("Hệ thống giải thưởng", "Sun\* Kudos") | Montserrat | Medium 500 | 22sp | 28sp | 0% | `ButtonPrimaryBg` (#FFEA9E) |
| Theme description (mms_3_note) | Montserrat | Light 300 | 14sp | 20sp | 0.25px | white |
| Language label "VN" | Montserrat | Medium 500 | 14sp | 20sp | 0px | white |
| Button label — primary ("ABOUT AWARD", "Chi tiết") | Montserrat | Medium 500 | 14sp | 20sp | 0px | `TextOnButton` (#00101A) |
| Button label — secondary ("ABOUT KUDOS") | Montserrat | Medium 500 | 14sp | 20sp | 0px | white (#FFFFFF) |
| FAB separator "/" | Montserrat | Regular 400 | 24sp | 32sp | 0px | `TextOnButton` (#00101A) |
| Kudos badge "ĐIỂM MỚI CỦA SAA 2025" | Montserrat | Light 300 | 14sp | 20sp | 0.25px | white |
| Kudos description | Montserrat | Light 300 | 14sp | 20sp | 0.25px | white |

> **Custom extension needed**: `Typography.homeCountdownNumber`, `Typography.homeSectionTitle`, `Typography.homeSectionLabel` — add to `ui/theme/Type.kt` similar to `loginButton`.

---

## 4. Components

### 4.1 mms_1_header (Node 6885:9057)

**Container**
| Property | Value |
|----------|-------|
| Width | `375dp` (fillMaxWidth) |
| Height | `104dp` (44dp status bar + 60dp action bar) |
| Background | `linear-gradient(180deg, #00101A 0%, rgba(0,16,26,0.30) 76.44%, rgba(0,16,26,0.20) 84.62%, rgba(0,16,26,0.15) 88.7%, rgba(0,16,26,0.10) 92.79%, rgba(0,16,26,0.05) 96.39%, transparent 100%)` |
| Opacity | `0.9` |
| Position | Fixed (TopAppBar or Box at top of screen) |

**Logo** (mm_media_logo, `I6885:9057;88:1827`)
| Property | Value |
|----------|-------|
| Size | `48×44dp` |
| Position | `start=20dp`, `top=52dp` (below status bar) |
| Asset | `mm_media_logo_homepage.png` |
| ContentScale | `Cover` |

**Actions row** (`I6885:9057;88:1828`)
| Property | Value |
|----------|-------|
| Width | `122dp` |
| Height | `32dp` |
| Position | `end=355dp`, vertically centered in nav bar area |
| Gap between items | `10dp` |
| Alignment | `CenterVertically` |

**Language Switcher** (`I6885:9057;88:1829`)
| Property | Value |
|----------|-------|
| Width | `90dp` |
| Height | `32dp` |
| Background | — (transparent) |
| Border radius | `4dp` |
| Padding | `4dp top/bottom, 8dp start` |
| Gap | `8dp` |
| Flag icon | `MM_MEDIA_IC VN Flag` — `24×24dp` |
| Text "VN" | Montserrat Medium 500, 14sp, white, `22×20dp` |
| Dropdown arrow | `MM_MEDIA_Down` icon — `24×24dp` |

**Search icon** (`I6885:9057;88:1869`)
| Property | Value |
|----------|-------|
| Size | `24×24dp` |

**Notification Bell** (`I6885:9057;88:1830`)
| Property | Value |
|----------|-------|
| Size | `24×24dp` |

**Notification Badge** (`72:1628`)
| Property | Value |
|----------|-------|
| Size | `8×8dp` |
| Shape | `RoundedCornerShape(100dp)` (full circle) |
| Color | `#D4271D` |
| Position | `top-end` of Bell icon |

---

### 4.2 mms_2_content (Node 6885:8983)

**Container**
| Property | Value |
|----------|-------|
| Width | `335dp` |
| Height | `453dp` |
| Padding | horizontal: `start=20dp` from screen edge |
| Gap (children) | `32dp` |
| Direction | Column |

**ROOT FURTHER Logo** (Node 6885:8984, `I6885:8984;65:1590`)
| Property | Value |
|----------|-------|
| Width | `247dp` |
| Height | `109dp` |
| Asset | `mm_media_rootfurther_logo.png` (or equivalent) |
| ContentScale | `Cover` |

**countdown time** (Node 6885:8986)
| Property | Value |
|----------|-------|
| Width | `335dp` |
| Height | `112dp` |
| Gap (children) | `8dp` |
| Direction | Column |

**"Coming soon" text** (Node 6885:8987)
| Property | Value |
|----------|-------|
| Font | Montserrat Light 300, 14sp, lineHeight 20sp, letterSpacing 0.25px |
| Color | white |
| Visibility | Shown before event date; hidden after |

**Countdown units row** (Node 6885:8988)
| Property | Value |
|----------|-------|
| Gap | `16dp` |
| Each unit size | `72×84dp` |
| Direction | Row |

Each countdown unit (DAYS / HOURS / MINUTES):
- Flip-clock style number display (from Figma image)
- Number: Montserrat Bold 700, ~40sp, white, dark background tile
- Label: Montserrat Regular 400, ~12sp, white, below number

**Event info** (Node 6885:9016)
| Item | Style |
|------|-------|
| "Thời gian: 26/12/2025" | Montserrat Light 300, 14sp, white; "26/12/2025" bold (Gold color) |
| "Địa điểm: Âu Cơ Art Center" | Montserrat Light 300, 14sp, white |
| Livestream text | Montserrat Light 300, 14sp, white |

**mms_2.2_Button — Primary style** ("ABOUT AWARD", Node 6885:9026)
| Property | Value |
|----------|-------|
| Width | `160dp` |
| Height | `40dp` |
| Background | `rgba(255,234,158,1)` = solid `ButtonPrimaryBg` (#FFEA9E) |
| Border | none |
| Border radius | `4dp` |
| Padding | `12dp` all sides |
| Gap (icon + label) | `8dp` |
| Label | Montserrat Medium 500, 14sp, `TextOnButton` (#00101A) |
| Icon | Arrow icon, `24×24dp` |

**mms_2.3_Button — Secondary/Outline style** ("ABOUT KUDOS", Node 6885:9027)
| Property | Value |
|----------|-------|
| Width | `159dp` |
| Height | `40dp` |
| Background | `rgba(255,234,158,0.10)` = 10% transparent #FFEA9E |
| Border | `1dp solid #998C5F` |
| Border radius | `4dp` |
| Padding | `10dp` all sides |
| Gap (icon + label) | `8dp` |
| Label | Montserrat Medium 500, 14sp, **white** (#FFFFFF) |
| Icon | Arrow icon, `24×24dp` |

**Actions row** (Node 6885:9025): `gap=16dp` (confirmed from Figma), `flexDirection=row`, `justifyContent=flex-start`, `width=335dp`, `height=40dp`

---

### 4.3 mms_3_note (Node 6885:9028)

| Property | Value |
|----------|-------|
| Width | `335dp` |
| Font | Montserrat Light 300, 14sp, lineHeight 20sp, letterSpacing 0.25px |
| Color | white |
| Margin left | `20dp` from screen edge |

---

### 4.4 Section Header — mms_4.1 / mms_5.1 (shared component mms_4.1_header / mms_5.1_header)

| Property | Value |
|----------|-------|
| Width | `335dp` |
| Height | `53dp` |
| Direction | Column, gap: `4dp` |

| Sub-element | Style |
|-------------|-------|
| Event label (e.g. "Sun* Annual Awards 2025") | Montserrat Regular 400, 12sp, 16sp lineHeight, white |
| Divider (Rectangle 26) | 1dp height, full width, color `#2E3940` |
| Section title (e.g. "Hệ thống giải thưởng") | Montserrat Medium 500, 22sp, 28sp lineHeight, `ButtonPrimaryBg` (#FFEA9E) |

---

### 4.5 mms_4_awards (Node 6885:9030)

| Property | Value |
|----------|-------|
| Content width | `1040dp` (horizontally scrollable) |
| Height | `375dp` |
| Gap | `24dp` |
| Direction | Column |

**Award Card** (each, Node 6885:9033–9035, component 6885:8051)
- Contains: thumbnail image, award name, description (truncated with ellipsis), "Chi tiết" link + arrow icon
- "Chi tiết" link color: `ButtonPrimaryBg` (#FFEA9E)

---

### 4.6 mms_5.2_mm_media_Sunkudos — Kudos Banner (Node 6885:9041)

| Property | Value |
|----------|-------|
| Width | `335dp` |
| Height | `145dp` |
| Background (Rectangle) | `#0F0F0F` + mm_media_kudos_background.png (no-repeat, positioned) |
| Border radius | `5.dp` *(rounded from Figma 4.653dp — sub-pixel Figma value)* |
| Logo overlay (MM_MEDIA_Logo/Kudos) | `118×21dp`, positioned end-right |

**mms_5.3_Button — Primary style** ("Chi tiết", Node 6885:9055)
| Property | Value |
|----------|-------|
| Width | `160dp` |
| Height | `40dp` |
| Background | solid `ButtonPrimaryBg` (#FFEA9E) |
| Border radius | `4dp` |
| Padding | `12dp` all sides |
| Gap (icon + label) | `8dp` |
| Label | Montserrat Medium 500, 14sp, `TextOnButton` (#00101A) |
| Icon | Arrow icon, `24×24dp` |

---

### 4.7 mms_6_float button — FAB (Node 6885:9058)

| Property | Value |
|----------|-------|
| Width | `89dp` |
| Height | `48dp` |
| Background | `ButtonPrimaryBg` (#FFEA9E) |
| Shape | `RoundedCornerShape(100dp)` (pill) |
| Padding | `8dp` all sides |
| Direction | Row |
| Gap | `8dp` |
| Box shadow | `0 4dp 4dp rgba(0,0,0,0.25)` + `0 0 6dp #FAE287` (outer glow) |
| Position | Fixed, `bottom-right`, `end=20dp`, `bottom` above nav bar |

| Sub-element | Size | Style |
|-------------|------|-------|
| MM_MEDIA_Pen (pencil icon) | 24×24dp | Icon |
| "/" separator | — | Montserrat Regular 400, 24sp, 32sp lineHeight, `TextOnButton` (#00101A) |
| MM_MEDIA_IC_Kudos Logo | 24×24dp | Icon |

---

### 4.8 mms_7_nav bar — Bottom Navigation (Node 6885:9056)

| Property | Value |
|----------|-------|
| Width | `375dp` (fillMaxWidth) |
| Height | `72dp` |
| Background | `rgba(255, 234, 158, 0.15)` — semi-transparent yellow tint |
| Border radius | `topStart=20dp, topEnd=20dp` |
| Backdrop filter | `blur(20dp)` — use `BlurMaskFilter` or Compose blur modifier |
| Padding | `horizontal=24dp` |
| Position | Fixed bottom |

**Tabs row** (Node `I6885:9056;75:2008`)
| Property | Value |
|----------|-------|
| Height | `54dp` |
| Direction | Row, `SpaceBetween` |
| Each tab | `60×44dp`, Column, gap `4dp`, CenterHorizontally |

| Tab | Label | State (active vs inactive) |
|-----|-------|---------------------------|
| SAA 2025 | "SAA 2025" | Active on Home screen |
| Awards | "Awards" | Inactive on Home screen |
| Kudos | "Kudos" | Inactive on Home screen |
| Profile | "Profile" | Inactive on Home screen |

Active tab: icon + label in `ButtonPrimaryBg` (#FFEA9E); Inactive: white at reduced opacity.

---

## 5. Layout Structure & Spacing

```
Screen (375dp wide)
│
├── Box (fills screen, z=0) — Background layers (mm_media_bg group, always behind all content)
│     ├── Image: mm_media_keyvisual_bg.png (full bleed, CenterEnd)
│     ├── Box: Shadow Left gradient (horizontal, left edge)
│     └── Box: Shadow Bottom gradient (vertical, bottom edge, node rotated in Figma)
│
├── TopAppBar (fixed, z-top)                     [0–104dp, full width]
│     header gradient bg, Logo + Actions
│
├── LazyColumn (scrollable content, z=1, drawn over background)
│     │
│     ├── mms_2_content                          [top=144dp, padding start=20dp]
│     │     gap=32dp children
│     │     ├── ROOT FURTHER logo (247×109dp)
│     │     ├── countdown time (gap=8dp)
│     │     │     ├── "Coming soon" (14sp Light)
│     │     │     └── countdown row (gap=16dp)
│     │     └── actions row (ABOUT AWARD + ABOUT KUDOS)
│     │
│     ├── mms_3_note                             [padding start=20dp]
│     │     theme description text (Light 14sp)
│     │
│     ├── mms_4_awards                           [padding start=20dp, horizontal scroll]
│     │     gap=24dp
│     │     ├── mms_4.1_header (53dp)
│     │     └── mms_4.2_award list (horizontal scroll, content 1040dp)
│     │
│     └── mms_5_kudos                            [padding start=20dp]
│           gap=24dp
│           ├── mms_5.1_header (53dp)
│           ├── mms_5.2 kudos banner (335×145dp)
│           ├── note (description + badge)
│           └── mms_5.3_Button "Chi tiết"
│
├── FAB (fixed, bottom-right)                    [end=20dp, above nav bar]
│     89×48dp pill, #FFEA9E, glow shadow
│
└── BottomNavBar (fixed bottom)                  [height=72dp, full width]
      rgba(FFEA9E, 0.15) blur bg, 4 tabs
```

---

## 6. Figma → Compose Token Mapping

| Figma value | Compose implementation |
|-------------|----------------------|
| Header gradient `#00101A → transparent` | `Brush.verticalGradient(...)` in `Box.background` |
| Shadow Left `#00101A → transparent` horizontal | `Brush.horizontalGradient(...)` |
| Primary button bg `#FFEA9E` solid | `ButtonDefaults.buttonColors(containerColor = ButtonPrimaryBg)` |
| Primary button text `#00101A` | `color = TextOnButton` |
| Secondary button bg `rgba(FFEA9E, 0.10)` + border `#998C5F` | `OutlinedButton` with `containerColor = ButtonPrimaryBg.copy(alpha=0.10f)`, `border = BorderStroke(1.dp, Color(0xFF998C5F))` |
| Secondary button text white | `color = MaterialTheme.colorScheme.onSurface` |
| Button radius `4dp` | `shape = RoundedCornerShape(4.dp)` |
| Section title `#FFEA9E` | `color = ButtonPrimaryBg` |
| Divider `#2E3940` | `HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)` |
| Nav bar `rgba(FFEA9E, 0.15)` | `background(Color(0xFFFFEA9E).copy(alpha=0.15f))` |
| Nav bar radius top | `clip(RoundedCornerShape(topStart=20.dp, topEnd=20.dp))` |
| FAB glow shadow | `Modifier.shadow(elevation=4.dp, ...) + outer glow via Canvas` |
| Notification badge dot `#D4271D` | `Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFD4271D)))` |

---

## 7. Assets

| Asset | Node | Dimensions | File |
|-------|------|-----------|------|
| SAA Logo | `I6885:9057;88:1827;65:1760` | 48×44dp | `mm_media_logo_homepage.png` ⚠️ download needed |
| Keyvisual BG | `6885:8980` | full-screen | `mm_media_keyvisual_bg.png` ✅ downloaded |
| ROOT FURTHER Logo | `I6885:8984;65:1590` | 247×109dp | `mm_media_logo_rootfuther.png` ⚠️ download needed |
| Kudos Background | `6885:9043` | 335×145dp | `mm_media_kudos_background.png` ⚠️ download needed |
| Kudos Logo | `6885:9045` | 118×21dp | embedded in banner group |
| VN Flag icon | `I6885:9057;88:1829;65:2466` | 24×24dp | icon component |
| Search icon | `I6885:9057;88:1869` | 24×24dp | icon component |
| Bell icon | `I6885:9057;88:1830` | 24×24dp | icon component |
| Pen icon (FAB) | `I6885:9058;75:2164` | 24×24dp | icon component |
| Kudos Logo icon (FAB) | `I6885:9058;75:2166` | 24×24dp | icon component |
| Award thumbnails | `6885:9033–9035` | per card | from API URL |

---

## 8. Accessibility

| Element | Implementation |
|---------|---------------|
| Language Switcher | `contentDescription = "Language: VN, tap to change"` |
| Search icon | `contentDescription = stringResource(R.string.cd_search)` |
| Bell icon | `contentDescription = "Notifications, $unreadCount unread"` |
| Notification badge | Visual only (screen reader reads count via Bell contentDescription) |
| Award card "Chi tiết" | `contentDescription = "Chi tiết về ${award.name}"` |
| FAB Pencil | `contentDescription = stringResource(R.string.cd_write_kudo)` |
| FAB S/Kudos | `contentDescription = stringResource(R.string.cd_kudos_feed)` |
| Countdown | Periodically announce to accessibility when time changes |
| Decorative images (keyvisual, logo) | `contentDescription = null` |
