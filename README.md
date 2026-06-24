
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


## FUNEETIS Steps

FUNEETIS consists of six main steps that transform structured use case specifications into executable end-to-end IoT test cases.
| Step   | Description                                | Supported by |
| ------ | ------------------------------------------ | ------------ |
| Step 1 | Parse use case specifications              | TestDataGen  |
| Step 2 | Extract scenarios and relevant information | TestDataGen  |
| Step 3 | Generate test data                         | TestDataGen  |
| Step 4 | Manual refinement / preparation            | Manual       |
| Step 5 | Generate executable test cases             | TestGen      |
| Step 6 | Manual execution setup / validation        | Manual       |
