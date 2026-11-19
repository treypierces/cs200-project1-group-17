# Demo Video Script (about 10 minutes)

Follows the rubric order. Start with `java -cp out chocan.Main --reset` so the sample data matches this script.
Each person demos and explains their own classes.

| Time | Speaker | Rubric item | What to show |
|------|---------|-------------|--------------|
| 0:00 | Clark | Intro, code quality, UML | Show the UML diagram. Same packages as our P3 design: boundary, control, entity, data, reports. A terminal calls a controller, the controller uses a repository, the repository holds the entities. `Main` creates them all. |
| 1:00 | Clark | Login and menus (10) | Operator terminal: type a wrong number (rejected), then `200000001`. Point out the operator menu. Explain `Terminal.start()` and `VerifyController`. |
| 2:00 | Adam | Member management (12) | Add a member with a bad ZIP (`3540`) to show `validateData()`, fix it, note the new number. Update it (change city, set status S, confirm). List members. Delete it with confirmation. Explain `MemberController` and `MemberRepository`. |
| 3:30 | Samuel | Provider management (12) | Add a provider, update the name (confirm), delete it. List providers. Explain `ProviderController`. Log out. |
| 4:30 | Richard | Provider terminal, member validation, billing (12) | Provider login `500000001`. Validate `100000003` (Member Suspended), `999999999` (Invalid Number), `100000001` (Validated). Bill for service for `100000001`: a future date (rejected), yesterday's date, `D` to view the directory, code `883948`, confirm, add a comment. Show the fee. Explain `ServiceController` and `ServiceRecordRepository`. |
| 6:00 | Samuel | Provider Directory (6) | From the provider menu, request the Provider Directory. Show the file in `reports/directory`. Explain `ProviderDirectory.sortByName()` and `writeFile()`. Log out. |
| 6:45 | Trey | Accounting procedure and reports (18) | Manager login `300000001`. Run the main accounting procedure. Show the files written and the summary, then open one member report, one provider report, and the EFT file. Explain `ReportController.runWeeklyAccounting()` and the `ReportFile` subclasses. |
| 8:15 | Trey | Manager reports (12) | Request member reports, provider reports, and the summary report one at a time. |
| 9:00 | Clark | Extra credit: persistence (+5) | Exit completely. Run `java -cp out chocan.Main` again (no `--reset`). Manager login, request provider reports: the service Richard billed is still there. Show the `data` folder. |
| 9:40 | All | Contributions | One line each: "I wrote ___." Show the GitHub commit history. |

Tip: increase the terminal font size before recording so the reports are readable.
