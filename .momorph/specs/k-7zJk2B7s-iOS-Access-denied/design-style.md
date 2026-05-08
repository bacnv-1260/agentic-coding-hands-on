# Design Style: [iOS] Access Denied

**Frame**: `k-7zJk2B7s`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Created**: 2026-05-05

---

## 1. Screen Layout

| Property | Value |
|----------|-------|
| Orientation | Portrait only |
| Background | `#00101A` (= `Background` design token) |
| Safe area | Respects system status bar (top) and navigation bar (bottom) via `Scaffold` |
| Scroll | None — fixed single-screen layout |
| Layout structure | `Column`, top-to-bottom: Header → Spacer → Image → Spacer → Divider → Button |

---

## 2. Color Tokens

All colors are from the existing `SaaDarkColorScheme` and shared design tokens. No new colors needed.

| Role | Token / Value | Usage |
|------|--------------|-------|
| Screen background | `Background = #00101A` | `Scaffold` background |
| Title text | `ButtonPrimaryBg = #FFEA9E` | `access_denied_title` text color |
| Description text | `TextOnDark = #FFFFFF` (`onSurface`) | `access_denied_description` text color |
| Back arrow icon | `TextOnDark = #FFFFFF` (`onSurface`) | `IconButton` tint |
| Divider | `outlineVariant` (Material3 dark scheme) | `HorizontalDivider` under title, above button |
| Button background | `ButtonPrimaryBg = #FFEA9E` | `Button` container color |
| Button text | `TextOnButton = #00101A` | `Button` label color |

---

## 3. Typography

| Element | Style | Font | Weight | Size |
|---------|-------|------|--------|------|
| Title "Access Denied" | `MaterialTheme.typography.titleLarge` | Montserrat (via Google Fonts) | **Bold** (`FontWeight.Bold`) | ~22sp |
| Description text | `MaterialTheme.typography.bodyMedium` | Default (Roboto fallback) | Normal | ~14sp |
| Button label | `MaterialTheme.typography.loginButton` (custom token) | Montserrat Medium | Medium | 14sp |

> `loginButton` is defined in `ui/theme/Type.kt`: `MontserratMedium, FontWeight.Medium, 14sp`

---

## 4. Components

### 4.1 mms_Header (Node 6885:9523)

| Sub-element | Property | Value |
|-------------|----------|-------|
| `back_arrow` | Component | `IconButton` + `Icons.AutoMirrored.Filled.ArrowBack` |
| `back_arrow` | Position | Top-left, `padding(start = 4.dp, top = 4.dp)` |
| `back_arrow` | Icon tint | `MaterialTheme.colorScheme.onSurface` (#FFFFFF) |
| `back_arrow` | Accessibility | `semantics { contentDescription = stringResource(R.string.cd_back_arrow) }` |
| Title | Text | `stringResource(R.string.access_denied_title)` |
| Title | Color | `ButtonPrimaryBg` (#FFEA9E) — gold/yellow |
| Title | Alignment | `TextAlign.Center` |
| Title | Weight | `FontWeight.Bold` |
| Title | Style | `MaterialTheme.typography.titleLarge` |
| Title | Padding | `horizontal = 16.dp, vertical = 8.dp` |
| Divider | Component | `HorizontalDivider` |
| Divider | Width | `fillMaxWidth()` |
| Divider | Color | `MaterialTheme.colorScheme.outlineVariant` |
| Description | Text | `stringResource(R.string.access_denied_description)` |
| Description | Color | `MaterialTheme.colorScheme.onSurface` (#FFFFFF) |
| Description | Alignment | `TextAlign.Center` |
| Description | Style | `MaterialTheme.typography.bodyMedium` |
| Description | Padding | `horizontal = 16.dp, vertical = 12.dp` |

### 4.2 mms_2.1 — Illustrative Image (Node 6885:9529)

| Property | Value |
|----------|-------|
| Current drawable | `R.drawable.mm_media_icon` (placeholder ⚠️) |
| Target drawable | `R.drawable.mm_media_not_found` (Blocker B2 — pending designer) |
| Size | `240.dp × 240.dp` |
| Alignment | `Alignment.CenterHorizontally` |
| `contentDescription` | `null` (decorative image) |
| Position | Vertically centered between header and divider/button, weighted with `Spacer(weight=1f)` |

### 4.3 Separator Divider (above button)

| Property | Value |
|----------|-------|
| Component | `HorizontalDivider` |
| Width | `fillMaxWidth()` |
| Color | `MaterialTheme.colorScheme.outlineVariant` |
| Purpose | Visual separator between image area and button area |

### 4.4 mms_2.2_Button (Node 6885:9531)

| Property | Value |
|----------|-------|
| Component | `Button` (Material3) |
| Label | `stringResource(R.string.access_denied_button)` |
| Label style | `MaterialTheme.typography.loginButton` |
| Label color | `TextOnButton` (#00101A) |
| Label alignment | `TextAlign.Center` |
| Container color | `ButtonPrimaryBg` (#FFEA9E) |
| Shape | `RoundedCornerShape(4.dp)` |
| Width | `fillMaxWidth()` |
| Padding | `horizontal = 16.dp, vertical = 16.dp` |
| State | Always **enabled** — no disabled state |

---

## 5. Spacing & Sizing

| Element | Value |
|---------|-------|
| Screen horizontal padding | `16.dp` (applied per component) |
| Back arrow padding | `start = 4.dp, top = 4.dp` |
| Title vertical padding | `vertical = 8.dp` |
| Description vertical padding | `vertical = 12.dp` |
| Image size | `240.dp × 240.dp` |
| Button vertical padding | `vertical = 16.dp` |
| Image centering | `Spacer(Modifier.weight(1f))` above and below image |

---

## 6. Figma → Compose Token Mapping

| Figma visual | Compose implementation |
|-------------|----------------------|
| Dark navy background `#00101A` | `SaaTheme` → `SaaDarkColorScheme.background = Background` |
| Gold title | `color = ButtonPrimaryBg` |
| White description text | `color = MaterialTheme.colorScheme.onSurface` |
| Yellow button `#FFEA9E` | `ButtonDefaults.buttonColors(containerColor = ButtonPrimaryBg)` |
| Dark button text `#00101A` | `color = TextOnButton` |
| Rounded button corners | `shape = RoundedCornerShape(4.dp)` |
| Horizontal lines | `HorizontalDivider(color = outlineVariant)` |
| Back arrow at top-left | `IconButton` with `Icons.AutoMirrored.Filled.ArrowBack` |

---

## 7. Asset References

| Asset | File | Status |
|-------|------|--------|
| Illustrative image (mms_2.1) | `res/drawable/mm_media_not_found.png` | ⚠️ **Draft** — not finalized. Using `mm_media_icon.png` as placeholder |
| Back arrow icon | `Icons.AutoMirrored.Filled.ArrowBack` (Material Icons) | ✅ Available |

---

## 8. Accessibility

| Element | Implementation |
|---------|---------------|
| `back_arrow` | `Modifier.semantics { contentDescription = stringResource(R.string.cd_back_arrow) }` on `IconButton`; `Icon` itself has `contentDescription = null` |
| Image (mms_2.1) | `contentDescription = null` — decorative, no semantic value |
| Button (mms_2.2) | Label text is the `contentDescription` by default — no extra semantics needed |
| TalkBack reading order | top → bottom: back_arrow → title → description → image (skipped) → button |
