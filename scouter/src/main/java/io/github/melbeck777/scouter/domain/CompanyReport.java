package io.github.melbeck777.scouter.domain;

import java.util.List;
import java.util.Objects;

/**
 * One company's securities report, parsed into a minimal domain model (M0).
 *
 * <p>Identity information (the company name) is a fixed field; the financial
 * figures are an open-ended list of {@link FinancialItem}. This "hybrid" shape
 * lets the parser copy CSV rows into {@code items} without the domain type
 * knowing every possible element id up front (see ADR 0001 follow-up).
 *
 * <p>Pure domain type: no Spring, no I/O. Instances are meant to be immutable
 * and safe to share.
 *
 * @param companyName the filer/company name from the report
 * @param items       the financial line items extracted from the CSV
 */
public record CompanyReport(String companyName, List<FinancialItem> items) {

    public CompanyReport {
        Objects.requireNonNull(companyName, "companyName");
        Objects.requireNonNull(items, "items");
        items = List.copyOf(items);
    }
}
