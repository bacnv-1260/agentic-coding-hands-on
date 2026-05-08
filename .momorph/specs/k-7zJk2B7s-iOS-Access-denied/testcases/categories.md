# Test Cases: [iOS] Access Denied

## 1. ACCESSING

| Category | Sub Category | Sub Sub Category |
| -------- | ------------ | ---------------- |
| Check navigation path | From Login screen | Via 403 / AccessDeniedException |
| Check access permission | Back stack behavior | Login cleared on arrival |

---

## 2. GUI

| Category | Sub Category | Sub Sub Category |
| -------- | ------------ | ---------------- |
| Check layout | Screen-wide layout | Overall structure |
| Initialize | mms_2.2_Button | Default value/state |

---

## 3. FUNCTION

| Category | Sub Category | Sub Sub Category |
| -------- | ------------ | ---------------- |
| Check component interaction | back_arrow | Tap |
| Check component interaction | mms_2.2_Button | Tap |
| Check navigation behavior | mms_2.2_Button tap | NavRoutes.Home |
| Check navigation behavior | back_arrow tap | Exit app |
| Check navigation behavior | System Back button | Exit app |
| Check business logic | Back stack | Login cleared on arrival (FR-002) |
| Check business logic | Back stack | Access Denied cleared on navigate to Home (FR-004) |
| Check business logic | Screen trigger | 403 / AccessDeniedException → Access Denied shown (FR-001) |
| Check business logic | Language localization | All text updates on language switch (FR-005) |
| Check state transition | mms_2.2_Button | Always enabled (no disabled state) |
| Check cross-component effect | Language switch | Access Denied text updates to selected language |
