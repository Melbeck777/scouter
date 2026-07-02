package io.github.melbeck777.scouter.domain;

/**
 * A single line of an EDINET securities-report CSV, kept as raw values.
 *
 * <p>Mirrors one data row of the {@code jpcrp...} CSV: the XBRL element id,
 * its human-readable Japanese label, the reported value, and its unit. The
 * value stays a {@code String} on purpose — converting to a number is a
 * concern of formatting/analysis, not of this minimal domain model (M0).
 *
 * <p>Pure domain type: no Spring, no I/O.
 *
 * @param elementId XBRL element id (e.g. {@code jpcrp_cor:NetSalesSummaryOfBusinessResults})
 * @param label     Japanese item name from the CSV ("項目名")
 * @param value     the reported value, as written in the CSV ("値")
 * @param unit      the unit of the value ("単位"), e.g. {@code JPY}
 */
public record FinancialItem(String elementId, String label, String value, String unit) {
}
