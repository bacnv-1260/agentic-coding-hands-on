package com.example.saa.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// mms_D.1.2 → kudosReceived  (Số Kudos bạn nhận được)
// mms_D.1.3 → kudosSent      (Số Kudos bạn đã gửi)
// mms_D.1.4 → heartsReceived (Số tim bạn nhận được)
// mms_D.1.6 → secretBoxesOpened   (Số Secret Box đã mở)
// mms_D.1.7 → secretBoxesUnopened (Số Secret Box chưa mở) — drives Open button state
// ─────────────────────────────────────────────────────────────────────────────

class UserStatsDtoTest {

    // ── TC-DTO-STATS-001: All fields present ─────────────────────────────────
    @Test
    fun `toDomain - all non-null fields are mapped correctly`() {
        val dto = UserStatsDto(
            id = "stats-1",
            kudosReceived = 5,
            kudosSent = 25,
            heartsReceived = 25,
            secretBoxesOpened = 25,
            secretBoxesUnopened = 3,
        )

        val domain = dto.toDomain()

        assertEquals(5, domain.kudosReceived)
        assertEquals(25, domain.kudosSent)
        assertEquals(25, domain.heartsReceived)
        assertEquals(25, domain.secretBoxesOpened)
        assertEquals(3, domain.secretBoxesUnopened)
    }

    // ── TC-DTO-STATS-002: kudosReceived null → 0 ─────────────────────────────
    // mms_D.1.2: count shows 0 when null
    @Test
    fun `toDomain - kudosReceived null maps to 0`() {
        val dto = UserStatsDto(
            id = "stats-2",
            kudosReceived = null,
            kudosSent = 10,
            heartsReceived = 5,
            secretBoxesOpened = 1,
            secretBoxesUnopened = 2,
        )

        assertEquals(0, dto.toDomain().kudosReceived)
    }

    // ── TC-DTO-STATS-003: kudosSent null → 0 ─────────────────────────────────
    // mms_D.1.3
    @Test
    fun `toDomain - kudosSent null maps to 0`() {
        val dto = UserStatsDto(
            id = "stats-3",
            kudosReceived = 5,
            kudosSent = null,
            heartsReceived = 5,
            secretBoxesOpened = 1,
            secretBoxesUnopened = 2,
        )

        assertEquals(0, dto.toDomain().kudosSent)
    }

    // ── TC-DTO-STATS-004: heartsReceived null → 0 ────────────────────────────
    // mms_D.1.4
    @Test
    fun `toDomain - heartsReceived null maps to 0`() {
        val dto = UserStatsDto(
            id = "stats-4",
            kudosReceived = 5,
            kudosSent = 10,
            heartsReceived = null,
            secretBoxesOpened = 1,
            secretBoxesUnopened = 2,
        )

        assertEquals(0, dto.toDomain().heartsReceived)
    }

    // ── TC-DTO-STATS-005: secretBoxesOpened null → 0 ─────────────────────────
    // mms_D.1.6
    @Test
    fun `toDomain - secretBoxesOpened null maps to 0`() {
        val dto = UserStatsDto(
            id = "stats-5",
            kudosReceived = 5,
            kudosSent = 10,
            heartsReceived = 5,
            secretBoxesOpened = null,
            secretBoxesUnopened = 2,
        )

        assertEquals(0, dto.toDomain().secretBoxesOpened)
    }

    // ── TC-DTO-STATS-006: secretBoxesUnopened null → 0 ───────────────────────
    // mms_D.1.7: Open button disabled when 0
    @Test
    fun `toDomain - secretBoxesUnopened null maps to 0`() {
        val dto = UserStatsDto(
            id = "stats-6",
            kudosReceived = 5,
            kudosSent = 10,
            heartsReceived = 5,
            secretBoxesOpened = 3,
            secretBoxesUnopened = null,
        )

        assertEquals(0, dto.toDomain().secretBoxesUnopened)
    }

    // ── TC-DTO-STATS-007: All nullable fields null ───────────────────────────
    @Test
    fun `toDomain - all nullable fields null produces all zeros`() {
        val dto = UserStatsDto(
            id = "stats-7",
            kudosReceived = null,
            kudosSent = null,
            heartsReceived = null,
            secretBoxesOpened = null,
            secretBoxesUnopened = null,
        )

        val domain = dto.toDomain()
        assertEquals(0, domain.kudosReceived)
        assertEquals(0, domain.kudosSent)
        assertEquals(0, domain.heartsReceived)
        assertEquals(0, domain.secretBoxesOpened)
        assertEquals(0, domain.secretBoxesUnopened)
    }

    // ── TC-DTO-STATS-008: secretBoxesUnopened > 0 enables Open button ────────
    // mms_D.1.7: derived button enabled logic
    @Test
    fun `toDomain - secretBoxesUnopened positive means button is enabled`() {
        val dto = UserStatsDto(
            id = "stats-8",
            kudosReceived = 0,
            kudosSent = 0,
            heartsReceived = 0,
            secretBoxesOpened = 0,
            secretBoxesUnopened = 1,
        )

        assertTrue(dto.toDomain().secretBoxesUnopened > 0)
    }

    // ── TC-DTO-STATS-009: secretBoxesUnopened == 0 disables Open button ──────
    @Test
    fun `toDomain - secretBoxesUnopened zero means button is disabled`() {
        val dto = UserStatsDto(
            id = "stats-9",
            kudosReceived = 0,
            kudosSent = 0,
            heartsReceived = 0,
            secretBoxesOpened = 5,
            secretBoxesUnopened = 0,
        )

        assertEquals(0, dto.toDomain().secretBoxesUnopened)
    }
}
