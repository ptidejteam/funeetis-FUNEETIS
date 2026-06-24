
# FUNEETIS

FUNEETIS is a framework for generating end-to-end functional tests for IoT systems from structured use case specifications and system descriptions.

The repository is organized around two main modules:

1. **TestDataGen**  
   Generates test data from use case specifications and related input files.

2. **TestGen**  
   Generates executable test cases using the system description and the test data produced by TestDataGen.

---

## Repository Structure

```text
FUNEETIS/
├── TestDataGen/
├── TestGen/
├── Authoring_Guidelines.pdf
├── WIMP_System_Description.pdf
├── EWS-FD_Description.pdf
├── RUCM.xlsx
├── sut.json
├── testdata_rucm1.json
├── testdata_rucm2.json
├── testdata_rucm3.json
├── prompt.txt
└── README.md


## FUNEETIS Workflow

FUNEETIS consists of six main steps that transform structured use case specifications into executable end-to-end IoT test cases.

| Step | Description | Type | Module |
|------|-------------|------|--------|
| 1 | Parse use case specifications and extract relevant information | Automated | TestDataGen |
| 2 | Extract test scenarios from use case specifications | Automated | TestDataGen |
| 3 | Generate test data for the extracted scenarios | Automated | TestDataGen |
| 4 | Review and refine generated artifacts when needed | Manual | N/A |
| 5 | Generate executable test cases using the system description and generated test data | Automated | TestGen |
| 6 | Configure, execute, and validate generated tests in the target environment | Manual | N/A |

### Workflow Overview

```text
Use Case Specifications
          │
          ▼
    TestDataGen
   (Steps 1–3)
          │
          ▼
    testdata.json
          │
          ▼
  Manual Refinement
      (Step 4)
          │
          ▼
       TestGen
      (Step 5)
          │
          ▼
 Executable Tests
          │
          ▼
 Test Execution &
   Validation
    (Step 6)
