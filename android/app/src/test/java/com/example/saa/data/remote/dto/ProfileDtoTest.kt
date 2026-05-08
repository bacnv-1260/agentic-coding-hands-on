package com.example.saa.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// mms_1.1_member  → ProfileDto.toDomain() drives ProfileInfoSection display
// mms_A.2_Name    → fullName / employeeCode / badgeType
// mms_1.1_member  → avatarUrl (async image)
// ─────────────────────────────────────────────────────────────────────────────

class ProfileDtoTest {

    // ── TC-DTO-PROFILE-001: All fields present ───────────────────────────────
    @Test
    fun `toDomain - all non-null fields are mapped correctly`() {
        val dto = ProfileDto(
            id = "user-1",
            fullName = "Huỳnh Dương Xuân Nhật",
            employeeCode = "CEVC3",
            avatarUrl = "https://example.com/avatar.jpg",
            badgeType = "Legend Hero",
            heroTier = 3,
            departmentId = "dept-99",
        )

        val domain = dto.toDomain()

        assertEquals("user-1", domain.id)
        assertEquals("Huỳnh Dương Xuân Nhật", domain.fullName)
        assertEquals("CEVC3", domain.employeeCode)
        assertEquals("https://example.com/avatar.jpg", domain.avatarUrl)
        assertEquals("Legend Hero", domain.badgeType)
        assertEquals(3, domain.heroTier)
    }

    // ── TC-DTO-PROFILE-002: fullName null → empty string ────────────────────
    // mms_A.2_Name: guard against null fullName crash
    @Test
    fun `toDomain - fullName null maps to empty string`() {
        val dto = ProfileDto(
            id = "user-2",
            fullName = null,
            employeeCode = "CEVC3",
            avatarUrl = null,
            badgeType = "Legend Hero",
            heroTier = 1,
            departmentId = null,
        )

        assertEquals("", dto.toDomain().fullName)
    }

    // ── TC-DTO-PROFILE-003: employeeCode null → empty string ─────────────────
    @Test
    fun `toDomain - employeeCode null maps to empty string`() {
        val dto = ProfileDto(
            id = "user-3",
            fullName = "Alice",
            employeeCode = null,
            avatarUrl = null,
            badgeType = null,
            heroTier = null,
            departmentId = null,
        )

        assertEquals("", dto.toDomain().employeeCode)
    }

    // ── TC-DTO-PROFILE-004: avatarUrl null preserved as null ─────────────────
    // mms_1.1_member: placeholder icon shown when null
    @Test
    fun `toDomain - avatarUrl null stays null`() {
        val dto = ProfileDto(
            id = "user-4",
            fullName = "Bob",
            employeeCode = "E001",
            avatarUrl = null,
            badgeType = null,
            heroTier = 0,
            departmentId = null,
        )

        assertNull(dto.toDomain().avatarUrl)
    }

    // ── TC-DTO-PROFILE-005: badgeType null → empty string ────────────────────
    // mms_A.2_Name: badge chip hidden when empty
    @Test
    fun `toDomain - badgeType null maps to empty string`() {
        val dto = ProfileDto(
            id = "user-5",
            fullName = "Carol",
            employeeCode = "E002",
            avatarUrl = null,
            badgeType = null,
            heroTier = 0,
            departmentId = null,
        )

        assertEquals("", dto.toDomain().badgeType)
    }

    // ── TC-DTO-PROFILE-006: heroTier null → 0 ────────────────────────────────
    // mms_2_icon collection: 0 active icon slots when null
    @Test
    fun `toDomain - heroTier null maps to 0`() {
        val dto = ProfileDto(
            id = "user-6",
            fullName = "Dave",
            employeeCode = "E003",
            avatarUrl = null,
            badgeType = "Hero",
            heroTier = null,
            departmentId = null,
        )

        assertEquals(0, dto.toDomain().heroTier)
    }

    // ── TC-DTO-PROFILE-007: heroTier value preserved ──────────────────────────
    // mms_2_icon collection: icon slots = heroTier
    @Test
    fun `toDomain - heroTier value is preserved`() {
        val dto = ProfileDto(
            id = "user-7",
            fullName = "Eve",
            employeeCode = "E004",
            avatarUrl = null,
            badgeType = "Legend",
            heroTier = 6,
            departmentId = null,
        )

        assertEquals(6, dto.toDomain().heroTier)
    }

    // ── TC-DTO-PROFILE-008: all nullable fields null ──────────────────────────
    @Test
    fun `toDomain - all nullable fields null produces safe defaults`() {
        val dto = ProfileDto(
            id = "user-8",
            fullName = null,
            employeeCode = null,
            avatarUrl = null,
            badgeType = null,
            heroTier = null,
            departmentId = null,
        )

        val domain = dto.toDomain()

        assertEquals("", domain.fullName)
        assertEquals("", domain.employeeCode)
        assertNull(domain.avatarUrl)
        assertEquals("", domain.badgeType)
        assertEquals(0, domain.heroTier)
    }
}
