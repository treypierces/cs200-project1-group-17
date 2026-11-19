"""Generates the Project 4 UML class diagram (Graphviz DOT). Class contents match the Java code."""
import html

C = {}

def cls(name, pkg, attrs, methods, stereo=None, abstract=False):
    C[name] = (stereo, abstract, pkg, attrs, methods)

S = "String"
# ----- boundary
cls("Terminal", "boundary", ["# userNumber : String", "# verified : boolean", "- scanner : Scanner"],
    ["+ start(numberPrompt : String) : void", "# verify(number : String) : boolean {abstract}",
     "# welcomeName(number : String) : String {abstract}", "+ showMainMenu() : void {abstract}", "+ display(message : String) : void",
     "+ readInput(prompt : String) : String", "# readChoice(prompt, min, max) : int", "# confirm(question : String) : boolean"],
    "boundary", True)
cls("ProviderTerminal", "boundary", ["- verifier : VerifyController", "- memberCtl : MemberController", "- serviceCtl : ServiceController"],
    ["+ verifyProvider(providerNumber : String) : boolean", "# verify(number) : boolean", "# welcomeName(number) : String", "+ showMainMenu() : void",
     "+ validateMember(memberNumber : String) : boolean", "+ billForService(memberNumber : String) : void",
     "+ requestProviderDirectory() : void", "- readServiceDate() : LocalDate"], "boundary")
cls("OperatorTerminal", "boundary", ["- verifier : VerifyController", "- memberCtl : MemberController", "- providerCtl : ProviderController"],
    ["+ verifyOperator(operatorNumber : String) : boolean", "# verify(number) : boolean", "# welcomeName(number) : String", "+ showMainMenu() : void",
     "+ addMember() : void", "+ updateMember(memberNumber : String) : void", "+ deleteMember(memberNumber : String) : void",
     "+ listMembers() : void", "+ addProvider() : void", "+ updateProvider(providerNumber : String) : void",
     "+ deleteProvider(providerNumber : String) : void", "+ listProviders() : void",
     "- enterData(current : Person, isMember : boolean) : Object[]"], "boundary")
cls("ManagerTerminal", "boundary", ["- verifier : VerifyController", "- reportCtl : ReportController"],
    ["+ verifyManager(managerNumber : String) : boolean", "# verify(number) : boolean", "# welcomeName(number) : String", "+ showMainMenu() : void", "+ runWeeklyAccounting() : void",
     "+ requestMemberReport() : void", "+ requestProviderReport() : void", "+ requestSummaryReport() : void"], "boundary")
# ----- control
cls("VerifyController", "control", ["- providerRepo : ProviderRepository", "- staffAccounts : List&lt;StaffAccount&gt;"],
    ["+ verifyProvider(providerNumber : String) : boolean", "+ verifyOperator(operatorNumber : String) : boolean",
     "+ verifyManager(managerNumber : String) : boolean", "+ getUserName(number : String) : String"], "control")
cls("MemberController", "control", ["- memberRepo : MemberRepository"],
    ["+ validateMember(memberNumber : String) : String", "+ validateData(name : String, address : Address) : String",
     "+ addMember(name : String, address : Address) : String", "+ getMember(memberNumber : String) : Member",
     "+ updateMember(memberNumber, name, address) : boolean", "+ deleteMember(memberNumber : String) : boolean",
     "+ updateMemberStatus(memberNumber : String, feesPaid : boolean) : boolean", "+ listMembers() : List&lt;Member&gt;"], "control")
cls("ProviderController", "control", ["- providerRepo : ProviderRepository"],
    ["+ validateData(name : String, address : Address) : String", "+ addProvider(name : String, address : Address) : String",
     "+ getProvider(providerNumber : String) : Provider", "+ updateProvider(providerNumber, name, address) : boolean",
     "+ deleteProvider(providerNumber : String) : boolean", "+ listProviders() : List&lt;Provider&gt;"], "control")
cls("ServiceController", "control", ["- directory : ProviderDirectory", "- recordRepo : ServiceRecordRepository"],
    ["+ getServiceName(serviceCode : String) : String",
     "+ billService(memberNumber, providerNumber, serviceDate, serviceCode, comments) : double",
     "+ requestProviderDirectory() : boolean", "+ getDirectoryListing() : String", "+ getDirectoryFileName() : String"], "control")
cls("ReportController", "control", ["- recordRepo : ServiceRecordRepository", "- memberRepo : MemberRepository",
    "- providerRepo : ProviderRepository", "- directory : ProviderDirectory", "- reportsDir : Path"],
    ["+ runWeeklyAccounting() : List&lt;ReportFile&gt;", "+ generateMemberReport() : List&lt;ReportFile&gt;",
     "+ generateProviderReport() : List&lt;ReportFile&gt;", "+ generateSummaryReport() : ReportFile",
     "+ generateEFTRecord() : ReportFile", "- groupByMember(records) : Map", "- groupByProvider(records) : Map",
     "- totalFee(records) : double"], "control")
# ----- entity
cls("Person", "entity", ["# name : String", "# number : String", "# address : Address"],
    ["+ getName() : String", "+ getNumber() : String", "+ getAddress() : Address",
     "+ update(name : String, address : Address) : void", "+ checkData(name, address) : String$"], "entity", True)
cls("Address", "entity", ["- street : String", "- city : String", "- state : String", "- zip : String"],
    ["+ Address(street, city, state, zip)", "+ getters", "+ check() : String", "+ toString() : String"], "entity")
cls("Member", "entity", ["- status : String  (ACTIVE / SUSPENDED)"],
    ["+ Member(number, name, address, status)", "+ getStatus() : String", "+ setStatus(status : String) : void",
     "+ isActive() : boolean", "+ toString() : String"], "entity")
cls("Provider", "entity", [], ["+ Provider(number, name, address)", "+ toString() : String"], "entity")
cls("StaffAccount", "entity", ["- staffNumber : String", "- role : String", "- name : String"],
    ["+ matches(staffNumber : String, role : String) : boolean", "+ getName() : String"], "entity")
cls("Service", "entity", ["- code : String", "- name : String", "- fee : double"],
    ["+ getCode() : String", "+ getName() : String", "+ getFee() : double"], "entity")
cls("ServiceRecord", "entity", ["- receivedDateTime : LocalDateTime", "- serviceDate : LocalDate",
    "- providerNumber : String", "- memberNumber : String", "- serviceCode : String", "- fee : double", "- comments : String"],
    ["+ ServiceRecord(serviceDate, providerNumber, memberNumber, serviceCode, fee, comments)", "+ getters"], "entity")
cls("ProviderDirectory", "entity", ["- services : List&lt;Service&gt;", "- dataFile : Path", "- fileName : Path"],
    ["+ findService(serviceCode : String) : Service", "+ getFee(serviceCode : String) : double",
     "+ sortByName() : void", "+ getListing() : String", "+ writeFile() : boolean", "+ getFileName() : Path", "+ money(amount : double) : String$"], "entity")
# ----- data
cls("MemberRepository", "data", ["- fileName : Path", "- members : List&lt;Member&gt;", "- nextNumber : long"],
    ["+ findMember(memberNumber : String) : Member", "+ getMemberStatus(memberNumber : String) : String",
     "+ nextMemberNumber() : String", "+ addMember(member : Member) : boolean", "+ saveMember(member : Member) : boolean",
     "+ removeMember(memberNumber : String) : boolean", "+ getAllMembers() : List&lt;Member&gt;"], "entity")
cls("ProviderRepository", "data", ["- fileName : Path", "- providers : List&lt;Provider&gt;", "- nextNumber : long"],
    ["+ findProvider(providerNumber : String) : Provider", "+ nextProviderNumber() : String",
     "+ addProvider(provider : Provider) : boolean", "+ saveProvider(provider : Provider) : boolean",
     "+ removeProvider(providerNumber : String) : boolean", "+ getAllProviders() : List&lt;Provider&gt;"], "entity")
cls("ServiceRecordRepository", "data", ["- fileName : Path", "- records : List&lt;ServiceRecord&gt;"],
    ["+ addRecord(record : ServiceRecord) : boolean", "+ getWeekRecords() : List&lt;ServiceRecord&gt;"], "entity")
cls("DataFile", "data", [], ["+ exists(file : Path) : boolean$", "+ readRows(file : Path) : List&lt;String[]&gt;$",
    "+ writeRows(file : Path, lines : List) : boolean$", "+ writeText(file : Path, text : String) : boolean$",
    "+ join(fields : String...) : String$"], "utility")
cls("SeedData", "data", [], ["+ staffAccounts() : List$", "+ members() : List$", "+ providers() : List$",
    "+ services() : List$", "+ records() : List$"], "utility")
# ----- reports
cls("ReportFile", "reports", ["# fileName : Path", "# reportDate : LocalDate"],
    ["+ getContent() : String {abstract}", "+ writeFile() : boolean", "+ getFileName() : Path", "# nameForFile(name : String) : String$", "# weekLine() : String", "# personBlock(label : String, p : Person) : String$", "# dashes(n : int) : String$"], "entity", True)
cls("MemberReport", "reports", ["- member : Member", "- services : List&lt;ServiceRecord&gt;"],
    ["+ MemberReport(member, services, providerRepo, directory, reportsDir)", "+ getContent() : String"], "entity")
cls("ProviderReport", "reports", ["- provider : Provider", "- services : List&lt;ServiceRecord&gt;",
    "- totalConsultations : int", "- totalFee : double"],
    ["+ ProviderReport(provider, services, memberRepo, reportsDir)", "+ getContent() : String"], "entity")
cls("SummaryReport", "reports", ["- byProvider : Map&lt;String, List&lt;ServiceRecord&gt;&gt;"],
    ["+ SummaryReport(byProvider, providerRepo, reportsDir)", "+ getContent() : String"], "entity")
cls("EFTRecord", "reports", ["- entries : List&lt;String&gt;"],
    ["+ EFTRecord(reportsDir)", "+ addEntry(providerName, providerNumber, amount) : void", "+ getContent() : String"], "entity")
# ----- app
cls("Main", "app", [], ["+ main(args : String[]) : void$"])

COL = {"boundary": "#F6ECFA", "control": "#FFF6E5", "entity": "#EAF2FB", "data": "#EEF7EA", "reports": "#FDEDEC", "app": "#F2F2F2"}

def row(t):
    static = t.endswith("$"); t = t.rstrip("$")
    t = t if "&lt;" in t else html.escape(t)
    return f'<TR><TD ALIGN="LEFT" BALIGN="LEFT">{"<U>"+t+"</U>" if static else t}</TD></TR>'

def node(n):
    st, ab, pkg, attrs, meths = C[n]
    title = f"<I><B>{n}</B></I>" if ab else f"<B>{n}</B>"
    head = f"&laquo;{st}&raquo;<BR/>" if st else ""
    a = "".join(row(x) for x in attrs) or "<TR><TD> </TD></TR>"
    m = "".join(row(x) for x in meths) or "<TR><TD> </TD></TR>"
    return (f'"{n}" [label=<<TABLE BORDER="1" COLOR="#333333" CELLBORDER="0" CELLSPACING="0" CELLPADDING="2">'
            f'<TR><TD>{head}{title}</TD></TR><HR/>{a}<HR/>{m}</TABLE>> style=filled fillcolor="{COL[pkg]}"];')

E = []
def inh(a, b): E.append(f'"{a}" -> "{b}" [arrowhead=empty weight=3];')
def comp(w, p, m): E.append(f'"{w}" -> "{p}" [arrowtail=diamond dir=back headlabel="{m}" taillabel="1"];')
def assoc(a, b, lbl="", m="1"): E.append(f'"{a}" -> "{b}" [arrowhead=vee xlabel="{lbl}" headlabel="{m}"];')
def dep(a, b, lbl): E.append(f'"{a}" -> "{b}" [arrowhead=vee style=dashed xlabel="{lbl}"];')

for t in ["ProviderTerminal", "OperatorTerminal", "ManagerTerminal"]: inh(t, "Terminal")
inh("Member", "Person"); inh("Provider", "Person")
for r in ["MemberReport", "ProviderReport", "SummaryReport", "EFTRecord"]: inh(r, "ReportFile")
comp("ProviderDirectory", "Service", "1..*"); comp("MemberRepository", "Member", "0..*")
comp("ProviderRepository", "Provider", "0..*"); comp("ServiceRecordRepository", "ServiceRecord", "0..*")
comp("Person", "Address", "1")
for t in ["ProviderTerminal", "OperatorTerminal", "ManagerTerminal"]: assoc(t, "VerifyController", "verifies with")
assoc("ProviderTerminal", "MemberController", "validates through"); assoc("ProviderTerminal", "ServiceController", "bills through")
assoc("OperatorTerminal", "MemberController", "maintains"); assoc("OperatorTerminal", "ProviderController", "maintains")
assoc("ManagerTerminal", "ReportController", "requests reports")
assoc("VerifyController", "ProviderRepository", "looks up"); assoc("VerifyController", "StaffAccount", "checks", "1..*")
assoc("MemberController", "MemberRepository", "uses"); assoc("ProviderController", "ProviderRepository", "uses")
assoc("ServiceController", "ProviderDirectory", "looks up codes in"); assoc("ServiceController", "ServiceRecordRepository", "saves to")
dep("ServiceController", "ServiceRecord", "creates")
for r in ["ServiceRecordRepository", "MemberRepository", "ProviderRepository", "ProviderDirectory"]: assoc("ReportController", r, "reads")
dep("ReportController", "ReportFile", "creates and writes")
assoc("MemberReport", "ProviderRepository", "names"); assoc("MemberReport", "ProviderDirectory", "names")
assoc("ProviderReport", "MemberRepository", "names"); assoc("SummaryReport", "ProviderRepository", "names")
for a, b in [("ServiceRecord", "Member"), ("ServiceRecord", "Provider"), ("ServiceRecord", "Service")]:
    E.append(f'"{a}" -> "{b}" [arrowhead=vee xlabel="by number" headlabel="1" taillabel="0..*"];')

out = ['digraph ChocAn {', 'rankdir=LR; newrank=true; nodesep=0.5; ranksep=1.4; splines=ortho; forcelabels=true;',
       'graph [fontname="Helvetica" fontsize=24 labelloc=t label="ChocAn System: Final UML Class Diagram (Team 17, Project 4)\\n'
       'Main creates every repository, controller, and terminal. Repositories and ProviderDirectory use DataFile to save and SeedData on first run. Underlined = static.\\n\\n"];',
       'node [shape=plain fontname="Helvetica" fontsize=11];', 'edge [fontname="Helvetica" fontsize=9 color="#555555"];']
for pkg in ["app", "boundary", "control", "data", "entity", "reports"]:
    label = "chocan" if pkg == "app" else "chocan." + pkg
    out.append(f'subgraph cluster_{pkg} {{ label="{label}"; fontsize=16; style=rounded; color="#999999";')
    out += [node(n) for n in C if C[n][2] == pkg]
    out.append("}")
out += E + ["}"]
open("/home/claude/uml2/ChocAn_UML.dot", "w").write("\n".join(out))
print("classes:", len(C))
