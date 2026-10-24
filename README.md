# MCP review sandbox

A small educational order-processing example in Java and Python. This is
original sample code prepared for a public PR-review exercise; it is not copied
from an employer project or a third-party repository.

The example uses integer cents, discounts, shipping, and aggregate reports.
The exercise branch intentionally includes defects. This repository is a test
target, not a payment system or production library.

Requirements: Java 21 or later; Python 3.10 or later. No external dependencies,
services, credentials, customer data, or package installation are needed.

Run Java from the repository root:

```powershell
javac --release 21 -d out java/sandbox/Money.java java/sandbox/OrderProcessor.java
java -cp out sandbox.OrderProcessor
```

Run Python:

```powershell
python python/invoice_report.py
```

Valid example inputs print deterministic totals. Passing those examples does
not establish that invalid-input handling or repeated reports are correct.
