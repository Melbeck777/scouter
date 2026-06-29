# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Project
scouter is a CLI tool that collects company information from public data
sources (EDINET, gBizINFO, Shokuba-lab) and generates a single Markdown report.
This is a personal **learning project**: treat the author's learning value as a
primary goal, not just task completion.

## Tech stack
- Java 25, Spring Boot
- Gradle (Groovy DSL)
- JUnit 5
- GitHub Actions (CI)

## Commands
- Build: `./gradlew build`
- Test:  `./gradlew test`
- Run:   `java -jar build/libs/scouter.jar`

## Collaboration rules (IMPORTANT)
Default mode: **explain first, implement minimally.**

1. For core areas, explain the approach, trade-offs, and options FIRST.
2. Do NOT write full implementations for core areas. Provide at most small
   skeletons, method signatures, or a few illustrative lines; let the author
   implement the rest.
3. Ask before writing any non-trivial code.

### Areas the AUTHOR implements (you only explain / review / suggest)
- HTTP client & API access (EDINET, gBizINFO)
- Data parsing (XBRL / CSV → value extraction)
- Domain model & overall design
- Markdown formatting & output
- Test strategy and the key/representative test cases

### Areas you MAY implement fully (boilerplate / low learning value)
- Build & project config (build.gradle, settings.gradle, wrapper)
- CI workflow (.github/workflows/ci.yml)
- Repetitive boilerplate (DTO accessors, trivial config) AFTER the design is fixed
- Repetitive/parameterized test cases AFTER the test approach is agreed
- Documentation formatting

When unsure whether something is "core" or "boilerplate", ASK.

## Language
- Conversational explanations to the author: **Japanese**.
- Code comments, commit messages, and committed docs: **English**.

## Conventions
- Comments, commit messages, and docs: **English only**.
- Commits: Conventional Commits (`feat:`, `fix:`, `test:`, `chore:`, ...).
- Keep changes small and reviewable; one logical change per PR.
- Package layout under `io.github.watanabemi.scouter` (standard Spring layout).

## Project structure
Keep dependencies pointing inward: `source` and `report` depend on `domain`,
never the reverse. `domain` is plain Java (no Spring, no I/O) so it stays easy
to test.

io/github/watanabemi/scouter/
├── ScouterApplication.java     # Spring Boot entry point
├── cli/                        # CommandLineRunner, argument parsing
├── domain/                     # Pure models + logic (no Spring, no I/O)
├── source/                     # Data acquisition & parsing (one pkg per source)
│   ├── edinet/                 # HTTP client + document parser (CSV/XBRL)
│   ├── gbizinfo/
│   └── shokuba/
├── pipeline/                   # Orchestration: fetch -> merge -> format
├── report/                     # Markdown rendering + file output
└── config/                     # Configuration (API keys, properties)

Notes:
- Start minimal (M0: cli + source/edinet + domain + report) and grow by adding
  new packages under `source/` without touching existing ones.
- Isolate all network / file / ZIP / CSV / XBRL handling inside `source/*`.
- Mirror this layout under src/test/java.

## Testing
- Tests must pass before merge (enforced by CI on PRs to `main`).
- Cover normal AND abnormal cases for parsing/formatting logic.
- The author owns the test strategy; you may fill repetitive cases only after
  the approach is agreed.

## Security & ethics
- Secrets (API keys/tokens) come from environment variables — never hard-code
  or commit them.
- Respect each data source's terms of use; cite sources in generated output.
- Do not scrape HTML; use the official APIs and CSV downloads only.

## Maintaining this file
- Treat this file as a living document. When a new convention or design
  decision is made, propose an update to CLAUDE.md in the same change.
