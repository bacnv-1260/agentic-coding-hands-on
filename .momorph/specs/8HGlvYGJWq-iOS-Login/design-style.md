# Design Style: [iOS] Login

**Frame ID**: `6885:8963`
**Screen ID**: `8HGlvYGJWq`
**Frame Name**: `[iOS] Login`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/8HGlvYGJWq
**Screen Image**: ![Login](https://momorph.ai/api/images/9ypp4enmFmdK3YAFJLIu6C/6885:8963/59e51999392a4363c206f36cdac92fe4.png)
**Extracted At**: 2026-05-05

---

## Design Tokens

### Colors

| Token Name | Hex Value | Opacity | Usage |
|------------|-----------|---------|-------|
| `--color-background` | `#00101A` | 100% | Screen background |
| `--color-header-gradient-start` | `#00101A` | 100% | Header gradient top (100%) |
| `--color-header-gradient-mid` | `#00101A` | 30% | Header gradient at 76% |
| `--color-header-gradient-end` | `#00101A` | 0% | Header gradient bottom (transparent) |
| `--color-button-primary-bg` | `#FFEA9E` | 100% | Login with Google button background |
| `--color-text-on-dark` | `#FFFFFF` | 100% | Description text, copyright, language label |
| `--color-text-on-button` | `#00101A` | 100% | Button label text |

> **Note**: The key visual background (`MM_MEDIA_Keyvisual BG`) is a full-screen image asset with abstract warm/cool gradient shapes (orange, teal, purple). All text and UI elements overlay this image.

### Typography

| Token Name | Font Family | Size | Weight | Line Height | Letter Spacing | Usage |
|------------|-------------|------|--------|-------------|----------------|-------|
| `--text-description` | Montserrat | 14px | 300 (Light) | 20px | 0.25px | `mms_4_content` — description body |
| `--text-button-label` | Montserrat | 14px | 500 (Medium) | 20px | 0px | `mms_5_Button` label |
| `--text-copyright` | Montserrat | 12px | 400 (Regular) | 16px | 0% | `mms_6_Copyright` |
| `--text-language-code` | (system) | — | — | — | — | Language code label (e.g., "VN") |

> **ROOT FURTHER** (`mms_3_MM_MEDIA_Logo/RootFuther`) is a static **image asset**, not a text element — no typography token applies.

### Spacing

| Token Name | Value | Usage |
|------------|-------|-------|
| `--spacing-screen-h-padding` | 20px | Horizontal inset from screen edge (logo, description, button) |
| `--spacing-header-top` | 52px | Distance from screen top to logo (status bar area) |
| `--spacing-header-height` | 104px | Full header section height |
| `--spacing-logo-size-w` | 48px | SAA logo width |
| `--spacing-logo-size-h` | 44px | SAA logo height |
| `--spacing-lang-sel-gap` | 8px | Gap between flag icon and language code in selector |
| `--spacing-lang-sel-padding` | `4px 0px 4px 8px` | Language selector internal padding |
| `--spacing-button-padding` | 12px | Login button internal padding (all sides) |
| `--spacing-button-gap` | 8px | Gap between button label and Google icon |
| `--spacing-footer-height` | 48px | Footer section height |
| `--spacing-footer-padding` | `16px 90px` | Footer vertical / horizontal padding |

### Border & Radius

| Token Name | Value | Usage |
|------------|-------|-------|
| `--radius-button` | 4px | Login with Google button (`mms_5_Button`) |
| `--radius-language-selector` | 4px | Language dropdown selector (`mms_2.1_language`) |
| `--radius-screen` | 0px | Screen frame (full-bleed) |

### Shadows

No explicit shadow tokens defined in this screen. The header uses a gradient overlay instead of a shadow for depth.

---

## Layout Specifications

### Screen Container

| Property | Value | Notes |
|----------|-------|-------|
| width | 375px | iPhone standard (375pt) |
| height | 812px | iPhone standard (812pt) |
| background | `#00101A` | Deep navy — behind key visual |
| overflow | hidden | Full-bleed layout |

### Layout Structure (ASCII)

```
┌────────────────── 375px ──────────────────┐
│  ┌──── Header (375×104px) ──────────────┐  │  y: 0–104
│  │  [Logo 48×44] (x:20)  [LangSel 90×32] (x:265) │
│  │  Gradient overlay: #00101A → transparent  │
│  └────────────────────────────────────────┘
│                                            │
│  ┌─── Key Visual BG (full-screen image) ──┐ │  y: 0–812
│  │  (abstract orange/teal/purple shapes)  │ │
│  └────────────────────────────────────────┘ │
│                                            │
│  [ROOT FURTHER image 247×109] (x:20, y:252)│  y: 252–361
│                                            │
│  [Description text 335×40] (x:20, y:393)  │  y: 393–433
│                                            │
│                                            │
│                                            │
│  ┌── Login Button (246×40, x:65) ─────┐   │  y: 626–666
│  │  "LOGIN With Google" + Google icon  │   │
│  └────────────────────────────────────┘   │
│                                            │
│  ┌── Footer (375×48) ─────────────────┐   │  y: 764–812
│  │     Copyright text (196×16, cx:187)│   │
│  └────────────────────────────────────┘   │
└────────────────────────────────────────────┘
```

### Spacing Between Sections

| Gap | Value | Between |
|-----|-------|---------|
| Header bottom → ROOT FURTHER top | 148px | `y:104` → `y:252` |
| ROOT FURTHER bottom → Description top | 32px | `y:361` → `y:393` |
| Description bottom → Button top | 193px | `y:433` → `y:626` |
| Button bottom → Footer top | 98px | `y:666` → `y:764` |

---

## Component Style Details

### `mms_2_mm_media_logo` — Sun* Annual Awards Logo

| Property | Value | Notes |
|----------|-------|-------|
| **Node ID** | `6885:8977` | — |
| type | INSTANCE (Image) | Static asset |
| width | 48px | — |
| height | 44px | — |
| position | absolute | x: 20px, y: 52px |
| padding | 0px | — |
| gap | 10px | Internal column layout |
| background | transparent | — |
| borderRadius | 0px | — |

---

### `mms_2.1_language` — Language Selector

| Property | Value | CSS |
|----------|-------|-----|
| **Node ID** | `6885:8976` | — |
| type | INSTANCE (Dropdown) | — |
| width | 90px | `width: 90px` |
| height | 32px | `height: 32px` |
| position | absolute | x: 265px, y: 64px |
| display | flex | `display: flex` |
| flexDirection | row | `flex-direction: row` |
| alignItems | center | `align-items: center` |
| justifyContent | flex-start | `justify-content: flex-start` |
| gap | 8px | `gap: 8px` |
| padding | `4px 0px 4px 8px` | `padding: 4px 0px 4px 8px` |
| borderRadius | 4px | `border-radius: 4px` |
| background | transparent | — |

**Children:**
- `country` frame: flag icon + language code text (e.g., "VN")
- `MM_MEDIA_Down` icon: dropdown arrow indicator

---

### `mms_3_MM_MEDIA_Logo/RootFuther` — ROOT FURTHER Branding

| Property | Value | Notes |
|----------|-------|-------|
| **Node ID** | `6885:8967` | — |
| type | INSTANCE (Image asset) | Not a text node |
| width | 247px | — |
| height | 109px | — |
| position | absolute | x: 20px, y: 252px |
| borderRadius | 0px | — |
| localization | None | Content fixed regardless of language |

---

### `mms_4_content` — Description Text

| Property | Value | CSS |
|----------|-------|-----|
| **Node ID** | `6885:8968` | — |
| type | TEXT | — |
| width | 335px | `width: 335px` |
| height | 40px | `height: 40px` |
| position | absolute | x: 20px, y: 393px |
| fontFamily | Montserrat | `font-family: 'Montserrat', sans-serif` |
| fontSize | 14px | `font-size: 14px` |
| fontWeight | 300 | `font-weight: 300` |
| lineHeight | 20px | `line-height: 20px` |
| letterSpacing | 0.25px | `letter-spacing: 0.25px` |
| textAlign | left | `text-align: left` |
| color | `#FFFFFF` | `color: white` |

**Localized Values:**
| Language | Content |
|----------|---------|
| VN | Bắt đầu hành trình của bạn cùng SAA 2025.\nĐăng nhập để khám phá! |
| EN | Start your journey with SAA 2025. Log in to explore! |
| JA | **[NEEDS TRANSLATION — yêu cầu từ Localization Team trước khi dev]** |

---

### `mms_5_Button` — Login with Google Button

| Property | Value | CSS |
|----------|-------|-----|
| **Node ID** | `6885:8969` | — |
| type | INSTANCE (Button — icon_text) | — |
| width | 246px | `width: 246px` |
| height | 40px | `height: 40px` |
| position | absolute | x: 65px (horizontally centered in 375px) |
| display | flex | `display: flex` |
| flexDirection | row | `flex-direction: row` |
| alignItems | center | `align-items: center` |
| justifyContent | center | `justify-content: center` |
| gap | 8px | `gap: 8px` |
| padding | 12px | `padding: 12px` |
| backgroundColor | `rgba(255, 234, 158, 1)` → `#FFEA9E` | `background-color: #FFEA9E` |
| borderRadius | 4px | `border-radius: 4px` |
| border | none | `border: none` |

**Label child** (`I6885:8969;28:1998`):

| Property | Value | CSS |
|----------|-------|-----|
| fontFamily | Montserrat | `font-family: 'Montserrat', sans-serif` |
| fontSize | 14px | `font-size: 14px` |
| fontWeight | 500 | `font-weight: 500` |
| lineHeight | 20px | `line-height: 20px` |
| letterSpacing | 0px | `letter-spacing: 0px` |
| textAlign | center | `text-align: center` |
| color | `#00101A` | `color: #00101A` |
| content | `"LOGIN With Google"` (trim trailing space từ Figma) | — |

**States:**

| State | Changes |
|-------|---------|
| Default | bg: `#FFEA9E`, cursor: pointer |
| Loading / Processing | Hiển thị `CircularProgressIndicator` inline; opacity: 0.7; pointer-events: none |
| Disabled (double-tap) | pointer-events: none trong suốt OAuth flow |

---

### `mms_6_Copyright` — Copyright Text

| Property | Value | CSS |
|----------|-------|-----|
| **Node ID** | `6885:8971` | — |
| type | TEXT | — |
| width | 196px | `width: 196px` |
| height | 16px | `height: 16px` |
| position | absolute | x: 89px (centered in 375px), y: 780px |
| fontFamily | Montserrat | `font-family: 'Montserrat', sans-serif` |
| fontSize | 12px | `font-size: 12px` |
| fontWeight | 400 | `font-weight: 400` |
| lineHeight | 16px | `line-height: 16px` |
| letterSpacing | 0% (0px) | `letter-spacing: 0px` |
| textAlign | center | `text-align: center` |
| color | `#FFFFFF` | `color: white` |

**Localized Values:**
| Language | Content |
|----------|---------|
| VN | Bản quyền thuộc về Sun* © 2025 |
| EN | Copyright belongs to Sun* © 2025 |
| JA | **[NEEDS TRANSLATION — yêu cầu từ Localization Team trước khi dev]** |

---

## Component Hierarchy with Styles

```
[iOS] Login Frame (375×812, bg: #00101A)
├── bg (GROUP)
│   └── MM_MEDIA_Keyvisual BG (RECTANGLE — full-screen image asset)
│
├── header (FRAME, 375×104, opacity: 0.9)
│   │   gradient: linear-gradient(180deg, #00101A 0% → transparent 100%)
│   ├── iOS/Component/StatusBar (INSTANCE — system status bar)
│   ├── mms_2_mm_media_logo (48×44, x:20, y:52)
│   └── mms_2.1_language (90×32, x:265, y:64, borderRadius:4, gap:8)
│       ├── country frame → flag icon + "VN" text
│       └── MM_MEDIA_Down (dropdown arrow icon)
│
├── mms_3_MM_MEDIA_Logo/RootFuther (247×109, x:20, y:252 — image asset)
│
├── mms_4_content (TEXT, 335×40, x:20, y:393)
│   Montserrat 14px/300/20px, color: #FFFFFF, letterSpacing: 0.25px
│
├── mms_5_Button (246×40, x:65, y:626, bg:#FFEA9E, borderRadius:4)
│   ├── Label (TEXT — "LOGIN With Google", Montserrat 14px/500, color:#00101A)
│   └── mm_media_icon (Google logo icon)
│
└── footer (FRAME, 375×48, y:764, padding:16px 90px)
    └── mms_6_Copyright (TEXT, 196×16, Montserrat 12px/400, color:#FFFFFF, centered)
```

---

## Responsive Specifications

This screen targets **iOS / Android mobile** only. No tablet or desktop layout is defined.

| Platform | Screen Size | Notes |
|----------|-------------|-------|
| iOS (base) | 375×812px | iPhone SE / 13 mini / 8 |
| iOS (large) | 390×844px+ | iPhone 14/15 Pro — layout may scale |
| Android | varies | Same logic, screen-relative positioning |

Key visual background scales to fill the full screen (`match_parent`). Button remains centered horizontally.

---

## Icon Specifications

| Icon Name | Node ID | Size | Usage |
|-----------|---------|------|-------|
| Vietnam flag | `I6885:8976;65:2466` | ~24px | Language selector — default VN state |
| Dropdown arrow | `I6885:8976;65:2468` | ~16px | Language selector expand indicator |
| Google logo | `I6885:8969;28:1997` | ~20px | Login button — beside label |

---

## Animation & Transitions

| Element | Property | Duration | Easing | Trigger |
|---------|----------|----------|--------|---------|
| `mms_5_Button` | opacity / loading state | 150ms | ease-in-out | Tap (OAuth start) |
| `mms_2.1_language` | dropdown expand | 200ms | ease-out | Tap on language selector |
| UI text | content swap | instant | — | Language selection change |
