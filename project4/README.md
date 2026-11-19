# ChocAn Data Processing System (CS 200 Project 4, Team 17)

Command line Java implementation of the Chocoholics Anonymous system, built from our Project 3 design
(boundary, control, entity, data, and reports packages). Data is saved to text files, so records persist
between program sessions.

## Requirements

Java JDK 8 or newer (`java -version` and `javac -version` should both work).

## Compile and run (Mac / Linux terminal)

From the `ChocAn` folder:

```
javac -d out $(find src -name "*.java")
java -cp out chocan.Main
```

## Compile and run (Windows Command Prompt)

From the `ChocAn` folder:

```
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out chocan.Main
```

## Eclipse / VS Code

Open the `ChocAn` folder as a Java project with `src` as the source folder, then run `chocan.Main`.
Set the working directory to the `ChocAn` folder so the `data` and `reports` folders are created there.

## Starting fresh

The first run loads sample data. To clear saved data and reload the sample data:

```
java -cp out chocan.Main --reset
```

## Logins (sample data)

| Terminal | Number |
|----------|--------|
| Provider | `500000001`, `500000002`, or `500000003` |
| Operator | `200000001` |
| Manager  | `300000001` |

Sample members: `100000001` to `100000005`. Member `100000003` (Carla Gomez) is suspended.
Sample service codes: `598470` Dietitian, `883948` Aerobics Exercise, and four others
(choose "Request Provider Directory" to see all).

## Features

| Terminal | Functions |
|----------|-----------|
| All      | Login by number with validation (3 attempts) |
| Provider | Validate member; bill for service; request Provider Directory |
| Operator | Add, update, delete, list members; add, update, delete, list providers |
| Manager  | Run main accounting procedure; member reports; provider reports; summary report |

Reports cover service records received in the last 7 days, including today.

## Output folders

| Folder | Contents |
|--------|----------|
| `data/` | Saved members, providers, services, and service records |
| `reports/members/` | Member reports (file name = member name + date) |
| `reports/providers/` | Provider reports (file name = provider name + date) |
| `reports/manager/` | Summary reports |
| `reports/eft/` | EFT data |
| `reports/directory/` | Provider Directory |

## Code structure

| Package | Classes |
|---------|---------|
| `chocan` | `Main` |
| `chocan.boundary` | `Terminal`, `ProviderTerminal`, `OperatorTerminal`, `ManagerTerminal` |
| `chocan.control` | `VerifyController`, `MemberController`, `ProviderController`, `ServiceController`, `ReportController` |
| `chocan.entity` | `Person`, `Address`, `Member`, `Provider`, `StaffAccount`, `Service`, `ServiceRecord`, `ProviderDirectory` |
| `chocan.data` | `MemberRepository`, `ProviderRepository`, `ServiceRecordRepository`, `DataFile`, `SeedData` |
| `chocan.reports` | `ReportFile`, `MemberReport`, `ProviderReport`, `SummaryReport`, `EFTRecord` |

The final UML class diagram is `docs/ChocAn_UML.pdf` (PNG copy included).

## Changes from the Project 3 design

| Change | Reason |
|--------|--------|
| No `Timer` class | A command line program is not running at midnight Friday, so the manager runs the main accounting procedure from the menu. |
| No `AcmeInterface` class | There is no Acme connection to simulate. The operator sets member status, which calls `MemberController.updateMemberStatus()`. |
| `Provider.type` removed | No report or screen uses it. |
| `validateData()` returns a String | It lists what is wrong so the operator can fix it. An empty string means valid. |
| Report methods return the report files | The manager terminal displays each report as well as saving it. |
| `ServiceRecord` stores the fee | Past records keep the fee they were billed at. |
| Added `DataFile`, `SeedData`, list methods, `getUserName()` | File saving, sample data, listing records, and the welcome message. |

## Team contributions

| Member | Classes |
|--------|---------|
| Clark Berry | `Main`, `Terminal`, `VerifyController`, `StaffAccount`, `Person`, `Address`, `DataFile`, `SeedData` |
| Adam Davis | `Member`, `MemberRepository`, `MemberController`, `OperatorTerminal` (member records) |
| Samuel Harvey | `Provider`, `ProviderRepository`, `ProviderController`, `Service`, `ProviderDirectory`, `OperatorTerminal` (provider records) |
| Richard Norman | `ServiceRecord`, `ServiceRecordRepository`, `ServiceController`, `ProviderTerminal` |
| Trey Pierce | `ReportController`, `ReportFile`, `MemberReport`, `ProviderReport`, `SummaryReport`, `EFTRecord`, `ManagerTerminal` |
