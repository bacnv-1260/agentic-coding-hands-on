# Viewpoints Library: [iOS] Access Denied

> Collected from test viewpoint templates matching the Access Denied screen's components.
> Applied strictly to behaviors explicitly described in specs.

---

## Source 1: Login — Login by Google account
**Relevance**: Pattern for Google OAuth failure/access denied states; navigation after auth failure.

| Viewpoint | Expected Result |
|-----------|----------------|
| Check first-time login when registered account is logging Google | Can login successfully |
| Check first-time login when OTHER account is logging Google | Handle auto-login or cannot login based on spec |
| Check when Google OAuth succeeds but domain is denied | Access Denied screen displayed |
| Check next-time login after Logout | Cannot auto-login; display login page |

**Applied to Access Denied**: Pattern for verifying navigation after 403 (domain denied) — confirms that Access Denied screen is shown and Login is cleared from stack.

---

## Source 2: Text_only — Fixed text
**Relevance**: Access Denied shows fixed static text (title + description) that must match spec/design.

| Viewpoint | Expected Result |
|-----------|----------------|
| Check fixed text/label displays corresponding with design, spec | Display correctly: position, format, value |
| Check fixed text/label unable to edit on screen | Cannot edit text by action on screen |

**Applied to Access Denied**:
- Title "Access Denied" / "Truy cập bị từ chối" must display exactly as specified
- Description text must display exactly as specified
- Button label "Go back to Home" / "Quay về trang chủ" must display exactly as specified
- Texts are static (read-only) — user cannot edit

---

## Source 3: Links — Normal Operation
**Relevance**: "Go back to Home" button behaves like an internal navigation link.

| Viewpoint | Expected Result |
|-----------|----------------|
| Confirm the link/button after click | Redirect to corresponding screen |
| Confirm redirection time (internal link) | Redirect immediately |
| Confirm no broken navigation | No broken navigation paths |

**Applied to Access Denied**:
- Tapping "Go back to Home" navigates to `NavRoutes.Home` immediately
- Home screen must be displayed after navigation
- Back stack must not contain Access Denied after navigation to Home

---

## Filtered Viewpoints (applicable to this screen)

The following viewpoints from reference screens are directly applicable based on spec:

1. **Static text correctness** (from Text_only): Title, description, and button text must match spec values in both VN and EN.
2. **Navigation outcome** (from Links): "Go back to Home" tap → Home screen displayed, no back-navigation to Access Denied.
3. **Authentication denial → error screen** (from Login/Google): When OAuth completes but domain is denied (403), Access Denied screen is shown.
4. **Back stack behavior**: Login cleared on arrival; system Back / back_arrow exits app.
5. **Localization**: All text elements update when language is switched (VN ↔ EN).
