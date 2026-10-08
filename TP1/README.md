# TP1 — TCP Communication Between Client and Server (Java)

A Java implementation of a TCP client-server architecture managing a university library catalog.

---

## Project Structure

```
TP1/
├── src/
│   ├── library/   # Domain logic (Livre, Bibliotheque, GestionBibliotheque)
│   ├── test/      # Local unit testing (TestBibliothequeLocal)
│   ├── ex2/       # Basic TCP PING/PONG (ServeurBibliothequeTCP, ClientBibliothequeTCP)
│   └── ex3/       # Full TCP Library Protocol (ServeurBibliotheque, ClientBibliotheque, ProtocoleBibliotheque)
└── bin/           # Compiled Java bytecode (.class files)
```

---

## Compilation

Compile all Java source files into the `bin/` directory:

### Windows (PowerShell)
```powershell
javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src).FullName
```

### Linux / macOS / Bash
```bash
find src -name "*.java" | xargs javac -d bin -sourcepath src
```

---

## Execution Commands

### Exercise 1: Local Domain Test
Run the local unit test suite without network sockets:
```bash
java -cp bin test.TestBibliothequeLocal
```

---

### Exercise 2: TCP PING / PONG

1. **Start Server** (Default port: `5000`):
   ```bash
   java -cp bin ex2.ServeurBibliothequeTCP 5000
   ```

2. **Start Client**:
   ```bash
   java -cp bin ex2.ClientBibliothequeTCP 127.0.0.1 5000
   ```

---

### Exercise 3: TCP Library Server & Client

1. **Start Library Server** (Default port: `5000`):
   ```bash
   java -cp bin ex3.ServeurBibliotheque 5000
   ```

2. **Start Library Client**:
   ```bash
   java -cp bin ex3.ClientBibliotheque 127.0.0.1 5000
   ```

---

## Supported Protocol Commands (Exercise 3)

- `LISTE` — Retrieve all catalog books.
- `RECHERCHE;<theme>` — Search books by topic (e.g., `RECHERCHE;JAVA`).
- `QUITTE` — Close the client connection.
