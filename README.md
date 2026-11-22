# Criptografia e Hash em Java — Estrutura de Dados II

Aplicação em modo texto (terminal) que demonstra:
- **Atividade 1:** criptografia e descriptografia com **AES-256-GCM** (chave derivada por PBKDF2).
- **Atividade 2:** geração e comparação de hash **SHA-256** (`MessageDigest`), com efeito avalanche.

## Estrutura de pastas

```
CriptografiaHash/
└── src/
    ├── main/       Main.java                      -> ponto de entrada (método main)
    ├── view/       MenuPrincipal.java             -> menu interativo e telas do terminal
    ├── service/    CriptografiaService.java       -> AES-256-GCM (criptografar/descriptografar)
    │               HashService.java               -> SHA-256 e comparação de hashes
    ├── model/      ResultadoCriptografia.java     -> dados da Atividade 1
    │               ResultadoHash.java             -> dados de um hash
    │               ComparacaoHash.java            -> resultado da comparação (iguais? bits diferentes)
    ├── utils/      ConversorUtil.java             -> bytes <-> Hex / Base64
    │               EntradaUtil.java               -> leitura e validação de entradas
    └── exception/  CriptografiaException.java     -> exceção própria do projeto
```

## Requisitos
- JDK 8 ou superior (testado com Java 21). Apenas bibliotecas nativas do Java.

## Como compilar e executar

Dentro da pasta `CriptografiaHash`:

**Windows (CMD/PowerShell)**
```
chcp 65001
mkdir out
javac -encoding UTF-8 -d out src\main\Main.java src\view\*.java src\service\*.java src\model\*.java src\utils\*.java src\exception\*.java
java -Dfile.encoding=UTF-8 -cp out main.Main
```

**Linux / macOS**
```
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out main.Main
```

**IDE (IntelliJ / Eclipse / VS Code):** abra a pasta, marque `src` como *Sources Root* e execute `main.Main`.
