# Design Style: [iOS] Sun*Kudos_Viết Kudo_default

**Frame ID**: `6885:9271`
**Screen ID**: `7fFAb-K35a`
**Frame Name**: `[iOS] Sun*Kudos_Viết Kudo_default`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/7fFAb-K35a
**Extracted At**: 2026-05-07

---

## Design Tokens

### Colors

| Token Name | Hex Value | Opacity | Usage |
|------------|-----------|---------|-------|
| `--color-background` | `#00101A` | 100% | Screen background (under keyvisual layer) |
| `--color-form-card-bg` | `#FFF8E1` | 100% | Form card background — warm cream/yellow |
| `--color-form-field-bg` | `#FFFFFF` | 100% | Individual input field background (B.2, B.4, D) |
| `--color-form-field-border` | `#CCCCCC` | 100% | Input field border (default state) |
| `--color-form-field-border-focus` | `#998C5F` | 100% | Input field border (focused state — golden brown) |
| `--color-form-field-placeholder` | `#999999` | 100% | Placeholder text color in all inputs |
| `--color-form-label` | `#00101A` | 100% | Field labels (Người nhận, Danh hiệu, etc.) |
| `--color-required-indicator` | `#E73928` | 100% | `*` asterisk on required labels (red) |
| `--color-header-text` | `#00101A` | 100% | Form header: "Gửi lời cám ơn và ghi nhận đến đồng đội" |
| `--color-community-link` | `#E73928` | 100% | "Tiêu chuẩn cộng đồng" text link (red) |
| `--color-hint-text` | `#666666` | 100% | Hint / hint-mention text below textarea |
| `--color-toolbar-icon` | `#333333` | 100% | Formatting toolbar icon color (default) |
| `--color-toolbar-icon-active` | `#00101A` | 100% | Formatting toolbar icon when active/toggled |
| `--color-toolbar-bg` | `#F0ECD6` | 100% | Formatting toolbar background (warm beige) |
| `--color-toolbar-border` | `#DDDDDD` | 100% | Toolbar bottom border separating from textarea |
| `--color-hashtag-chip-bg` | `#FFEA9E` | 30% | Hashtag chip/pill background (light gold tint) |
| `--color-hashtag-chip-border` | `#998C5F` | 100% | Hashtag chip border (golden brown) |
| `--color-hashtag-chip-text` | `#00101A` | 100% | Hashtag chip text (dark on light chip) |
| `--color-hashtag-add-btn-bg` | `#FFFFFF` | 100% | "+ Hashtag" button background |
| `--color-hashtag-add-btn-border` | `#998C5F` | 100% | "+ Hashtag" button border (golden brown) |
| `--color-hashtag-add-btn-text` | `#00101A` | 100% | "+ Hashtag" button label |
| `--color-image-add-btn-bg` | `#FFFFFF` | 100% | "+ Image" button background |
| `--color-image-add-btn-border` | `#998C5F` | 100% | "+ Image" button border |
| `--color-image-thumb-radius` | — | — | (see spacing) |
| `--color-anonymous-checkbox-off` | `#DDDDDD` | 100% | Checkbox unchecked (default) |
| `--color-anonymous-checkbox-on` | `#998C5F` | 100% | Checkbox checked (golden brown fill) |
| `--color-anonymous-label` | `#333333` | 100% | Anonymous toggle label text |
| `--color-button-cancel-bg` | `#1A1A1A` | 100% | "Hủy" cancel button background (near-black) |
| `--color-button-cancel-text` | `#FFFFFF` | 100% | Cancel button text/icon color |
| `--color-button-send-bg` | `#FFEA9E` | 100% | "Gửi đi" send button background (gold) |
| `--color-button-send-bg-disabled` | `#FFEA9E` | 40% | Send button disabled state (gold at 40% opacity) |
| `--color-button-send-text` | `#00101A` | 100% | Send button text color (dark on gold) |
| `--color-button-send-text-disabled` | `#00101A` | 40% | Send button text when disabled |
| `--color-nav-bar` | `#00070C` | 100% | Bottom navigation bar background |
| `--color-nav-active` | `#FFEA9E` | 100% | Active tab icon (Kudos tab is active) |
| `--color-nav-inactive` | `#FFFFFF` | 40% | Inactive tab icons |
| `--color-app-bar-bg` | transparent | — | App bar background (over keyvisual) |
| `--color-app-bar-text` | `#FFFFFF` | 100% | "New Kudo" title text + back icon color |

---

### Typography

| Token Name | Font Family | Size | Weight | Line Height | Letter Spacing | Usage |
|------------|-------------|------|--------|-------------|----------------|-------|
| `--text-app-bar-title` | Montserrat | 17px | 600 (SemiBold) | 22px | 0px | "New Kudo" navigation title |
| `--text-form-header` | Montserrat | 16px | 600 (SemiBold) | 22px | 0px | Form header: "Gửi lời cám ơn và ghi nhận đến đồng đội" — dark |
| `--text-field-label` | Montserrat | 14px | 500 (Medium) | 20px | 0px | Field labels: "Người nhận", "Danh hiệu", "Hashtag", "Image" |
| `--text-field-required` | Montserrat | 14px | 500 (Medium) | 20px | 0px | `*` required indicator (same weight as label, color: red) |
| `--text-field-placeholder` | Montserrat | 14px | 400 (Regular) | 20px | 0px | Placeholder text in B.2, B.4, D fields |
| `--text-field-value` | Montserrat | 14px | 400 (Regular) | 20px | 0px | User-entered text value in fields |
| `--text-hint` | Montserrat | 12px | 400 (Regular) | 16px | 0px | Hint text: "Ví dụ: Người truyền động lực..." and mention hint |
| `--text-community-link` | Montserrat | 13px | 500 (Medium) | 18px | 0px | "Tiêu chuẩn cộng đồng" text link |
| `--text-toolbar-icon` | Montserrat | 14px | 700 (Bold) | 14px | 0px | Toolbar icon labels (B, I) |
| `--text-hashtag-chip` | Montserrat | 13px | 400 (Regular) | 18px | 0px | Hashtag chip text |
| `--text-hashtag-add-btn` | Montserrat | 13px | 500 (Medium) | 18px | 0px | "+ Hashtag (Tối đa 5)" button label |
| `--text-image-add-btn` | Montserrat | 13px | 500 (Medium) | 18px | 0px | "+ Image (Tối đa 5)" button label |
| `--text-anonymous-label` | Montserrat | 14px | 400 (Regular) | 20px | 0px | "Gửi lời cám ơn và ghi nhận ẩn danh" |
| `--text-button-cancel` | Montserrat | 16px | 600 (SemiBold) | 22px | 0px | "Hủy" cancel button label |
| `--text-button-send` | Montserrat | 16px | 600 (SemiBold) | 22px | 0px | "Gửi đi" send button label |

---

### Spacing

| Token Name | Value | Usage |
|------------|-------|-------|
| `--spacing-screen-h-padding` | 20px | Horizontal margin of form card from screen edge |
| `--spacing-form-card-padding-h` | 16px | Horizontal internal padding of form card |
| `--spacing-form-card-padding-v` | 20px | Vertical internal padding of form card (top & bottom) |
| `--spacing-form-card-radius` | 12px | Border radius of form card |
| `--spacing-field-gap` | 16px | Vertical gap between form field rows |
| `--spacing-label-field-gap` | 6px | Gap between label and its input field |
| `--spacing-field-h-padding` | 12px | Horizontal padding inside input fields (B.2, B.4) |
| `--spacing-field-v-padding` | 10px | Vertical padding inside input fields |
| `--spacing-field-radius` | 6px | Border radius of input fields |
| `--spacing-field-height-single` | 44px | Height of single-line inputs (B.2, B.4) |
| `--spacing-textarea-min-height` | 100px | Minimum height of message textarea (D) |
| `--spacing-toolbar-height` | 36px | Height of formatting toolbar (C) |
| `--spacing-toolbar-icon-size` | 20px | Icon/button size in toolbar |
| `--spacing-toolbar-icon-gap` | 8px | Gap between toolbar buttons |
| `--spacing-hint-top` | 4px | Gap between textarea (D) and hint label (D.1) |
| `--spacing-hashtag-chip-h-padding` | 10px | Horizontal padding inside hashtag chip |
| `--spacing-hashtag-chip-v-padding` | 4px | Vertical padding inside hashtag chip |
| `--spacing-hashtag-chip-radius` | 16px | Border radius of hashtag chip (pill) |
| `--spacing-hashtag-chip-gap` | 8px | Gap between chips |
| `--spacing-hashtag-add-btn-h-padding` | 12px | Horizontal padding of "+ Hashtag" button |
| `--spacing-hashtag-add-btn-v-padding` | 6px | Vertical padding of "+ Hashtag" button |
| `--spacing-hashtag-add-btn-radius` | 6px | Border radius of "+ Hashtag" button |
| `--spacing-image-thumb-size` | 60px | Width × height of each image thumbnail |
| `--spacing-image-thumb-radius` | 6px | Border radius of image thumbnails |
| `--spacing-image-thumb-gap` | 8px | Gap between thumbnails |
| `--spacing-image-remove-btn-size` | 20px | Size of `×` remove button on thumbnail |
| `--spacing-anonymous-checkbox-size` | 20px | Width × height of anonymous checkbox |
| `--spacing-anonymous-row-gap` | 10px | Gap between checkbox and label |
| `--spacing-action-bar-height` | 52px | Height of bottom action row (Hủy + Gửi đi) |
| `--spacing-action-bar-gap` | 12px | Gap between Cancel and Send buttons |
| `--spacing-action-btn-radius` | 8px | Border radius of action buttons |
| `--spacing-action-btn-h-padding` | 16px | Horizontal padding inside action buttons |
| `--spacing-nav-bar-height` | 56px | Bottom navigation bar height |

---

### Border & Radius

| Token Name | Value | Usage |
|------------|-------|-------|
| `--radius-form-card` | 12px | Form card overall container |
| `--radius-field` | 6px | Input fields (B.2, B.4, D) |
| `--radius-toolbar` | 0px | Toolbar (no rounding; flat top) |
| `--radius-hashtag-chip` | 16px | Hashtag chip (full pill) |
| `--radius-hashtag-add-btn` | 6px | "+ Hashtag" add button |
| `--radius-image-thumb` | 6px | Image thumbnail |
| `--radius-action-btn` | 8px | Action buttons (Hủy / Gửi đi) |
| `--border-form-card` | none | Form card has no outer border |
| `--border-field-default` | `1px solid #CCCCCC` | Input field default state |
| `--border-field-focus` | `1px solid #998C5F` | Input field focused state |
| `--border-field-error` | `1px solid #E73928` | Input field validation error state |
| `--border-hashtag-chip` | `1px solid #998C5F` | Hashtag chip |
| `--border-hashtag-add-btn` | `1px solid #998C5F` | "+ Hashtag" button |
| `--border-image-add-btn` | `1px solid #998C5F` | "+ Image" button |

---

### Shadows

| Token Name | Value | Usage |
|------------|-------|-------|
| `--shadow-form-card` | `0px 4px 20px rgba(0,0,0,0.30)` | Drop shadow under form card |
| `--shadow-action-bar` | `0px -2px 8px rgba(0,0,0,0.20)` | Shadow above bottom action row |

---

## Layout Specifications

### Screen Container

| Property | Value | Notes |
|----------|-------|-------|
| Width | 375px | iPhone standard (375pt) |
| Background | keyvisual art + `#00101A` fallback | Art background fills full screen |
| Content | `ScrollView` (single scroll) | Form card is the only scrollable content |

### Layout Structure (ASCII)

```
┌────────────────── 375px ──────────────────┐
│  AppBar (transparent)                     │  h:44px
│  ← Back icon     "New Kudo"   (centered)  │  white text on keyvisual
├───────────────────────────────────────────┤
│  [Keyvisual Background — full viewport]   │  blurred / art image
│                                           │
│  ┌──────── Form Card (mx:20px) ─────────┐ │  radius:12px, bg:#FFF8E1
│  │  A  "Gửi lời cám ơn và ghi nhận..." │ │  16px SemiBold, dark
│  │                                      │ │
│  │  B.1 Người nhận *                   │ │  label 14px medium
│  │  ┌────────────────────────────────┐  │ │
│  │  │ Tìm kiếm                    ▼  │  │ │  B.2 dropdown h:44px
│  │  └────────────────────────────────┘  │ │
│  │                                      │ │
│  │  B.3 Danh hiệu *                    │ │  label 14px medium
│  │  ┌────────────────────────────────┐  │ │
│  │  │ Danh tặng một danh hiệu cho... │  │ │  B.4 text input h:44px
│  │  └────────────────────────────────┘  │ │
│  │  ⓘ Ví dụ: Người truyền động lực... │ │  hint 12px gray
│  │     Danh hiệu sẽ hiển thị làm...   │ │
│  │     [Tiêu chuẩn cộng đồng]  ←link  │ │  red text-link 13px
│  │                                      │ │
│  │  ┌─ C Toolbar ────────────────────┐  │ │  h:36px, bg:#F0ECD6
│  │  │  B   I   S̶   ☰  🔗  "  │  │ │  toolbar icons
│  │  └───────────────────────────────┘  │ │
│  │  ┌─ D Textarea ───────────────────┐  │ │  min-h:100px
│  │  │ Hãy gửi gắm lời cám ơn...    │  │ │  placeholder 14px gray
│  │  └───────────────────────────────┘  │ │
│  │  ⓘ Bạn có thể "@ + tên"...        │ │  hint 12px #666
│  │                                      │ │
│  │  E.1 Hashtag *                      │ │  label 14px medium
│  │  ┌─ E.2 Tag Group ────────────────┐  │ │
│  │  │  [+ Hashtag (Tối đa 5)]       │  │ │  add-btn + chips
│  │  └───────────────────────────────┘  │ │
│  │                                      │ │
│  │  F.1 Image                          │ │  label 14px medium
│  │  ┌─ F.2 ~ F.5 ───────────────────┐  │ │
│  │  │ [img×][img×][img×] [+Image]   │  │ │  60×60 thumbs + add-btn
│  │  └───────────────────────────────┘  │ │
│  │                                      │ │
│  │  ☐  Gửi lời cám ơn ẩn danh        │ │  G checkbox + label
│  └──────────────────────────────────────┘ │
│                                           │
├───────────────────────────────────────────┤
│  ┌──────────────┬───────────────────────┐ │  h:52px action bar
│  │  Hủy  ✕     │   Gửi đi  ▶           │ │  50%/50% split
│  │  dark bg     │   gold bg (#FFEA9E)    │ │
│  └──────────────┴───────────────────────┘ │
├───────────────────────────────────────────┤
│  Bottom NavBar                            │  h:56px, bg:#00070C
│  [SAA] [Awards] [Kudos●] [Profile]       │
└───────────────────────────────────────────┘
```

---

## Component States

### Input Field States

| State | Border | Background | Text |
|-------|--------|------------|------|
| Default (empty) | `1px solid #CCCCCC` | `#FFFFFF` | Placeholder `#999999` |
| Focused | `1px solid #998C5F` | `#FFFFFF` | Input text `#00101A` |
| Filled (valid) | `1px solid #CCCCCC` | `#FFFFFF` | Input text `#00101A` |
| Error | `1px solid #E73928` | `#FFF5F5` | Input text `#00101A`; error msg red below |

### Send Button States

| State | Background | Text | Enabled |
|-------|------------|------|---------|
| Disabled | `rgba(255,234,158,0.40)` | `rgba(0,16,26,0.40)` | No |
| Enabled | `#FFEA9E` | `#00101A` | Yes |
| Loading | `#FFEA9E` | spinner replaces label | No (submit in progress) |

### Formatting Toolbar Button States

| State | Icon Color | Background |
|-------|------------|------------|
| Default | `#333333` | Transparent |
| Active (toggled on) | `#00101A` | `rgba(0,0,0,0.08)` |
| Pressed | `#00101A` | `rgba(0,0,0,0.12)` |

### Hashtag Add Button States

| State | Border | Background |
|-------|--------|------------|
| Enabled (< 5 tags) | `1px solid #998C5F` | `#FFFFFF` |
| Disabled (= 5 tags) | `1px solid #CCCCCC` | `#F5F5F5` |

---

## Keyvisual Background

The full-screen background under the form card uses the same SAA 2025 keyvisual art asset as other screens in the app (`mm_media_bg`). The form card is layered on top with a drop shadow, making the colorful art visible around the card edges and through any transparent areas.

- Background image: `img_keyvisual_bg` (same asset as Kudos/Home screens)
- The card's cream color (`#FFF8E1`) provides clear visual separation from the dark keyvisual
- No blur or dim overlay applied to the background on this screen
