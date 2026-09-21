# MOTIVA - Sprint 3 de Programação Orientada a Objetos

Aplicação Java para monitorar a vegetação em trechos de rodovia, indicar o tipo de intervenção necessário e manter o histórico dos relatórios em um banco de dados Oracle.

O projeto continua o trabalho da Sprint 2. A hierarquia de intervenções, o monitoramento IoT e as regras de prioridade foram mantidos, agora com persistência por JDBC.

## Equipe

| Nome | RM |
|---|---|
| Bruno Anselmo da Silva | RM 566521 |
| Fernando de Almeida Godoi | RM 564820 |
| Gabriel Ber Soares | RM 563520 |
| Guilherme de Freitas Salgado | RM 562494 |
| Vinicius Ribeiro Dias | RM 566468 |

## Funcionalidades implementadas

- cadastro e consulta de equipes de manutenção;
- cadastro de trechos com altura da vegetação, ambiente e risco operacional;
- registro das intervenções de roçada manual, roçada mecanizada e pulverização;
- monitoramento de trechos por sensor IoT;
- classificação dos trechos em urgente, crítico, atenção ou normal;
- geração do relatório de prioridades no console;
- gravação e consulta do histórico de relatórios no Oracle;
- operações de inserir, buscar, listar, atualizar e excluir para todas as entidades persistidas.

## Implementação técnica

- Java 17;
- JDBC puro, sem Spring ou JPA;
- driver Oracle `ojdbc17.jar` na pasta `lib`;
- conexão centralizada pela classe `ConexaoBD`, no padrão Singleton;
- DAOs separados para equipe, trecho, intervenção e relatório;
- records usados para transportar os dados entre o banco e a aplicacao;
- comandos SQL parametrizados com `PreparedStatement`;
- fechamento automático de `PreparedStatement` e `ResultSet` com `try-with-resources`.

## Regras de prioridade

| Condição | Prioridade | Intervenção |
|---|---|---|
| Vegetação com 80 cm ou mais e risco alto | URGENTE | Roçada manual |
| Vegetação com 80 cm ou mais e risco baixo | CRÍTICO | Roçada mecanizada |
| Vegetação entre 40 cm e 79,9 cm | ATENÇÃO | Pulverização |
| Vegetação abaixo de 40 cm | NORMAL | Sem intervenção |

## Estrutura do projeto

```text
.
|-- lib/
|   `-- ojdbc17.jar
|-- src/br/com/motiva/
|   |-- dao/
|   |-- db/
|   |-- main/
|   |-- model/
|   `-- service/
|-- bd_motiva_criacao.sql
|-- bd_motiva_dados.sql
`-- README.md
```

### Pacotes

| Pacote | Responsabilidade |
|---|---|
| `db` | abertura e fechamento da conexão Oracle |
| `dao` | comandos de persistência e consultas SQL |
| `model` | classes do domínio e records do banco |
| `service` | motor de prioridade e geração do relatório |
| `main` | demonstração do funcionamento completo |

## Banco de dados

O banco possui quatro tabelas:

- `TB_EQUIPE_MANUTENCAO`;
- `TB_TRECHO_RODOVIA`;
- `TB_INTERVENCAO_OPERACIONAL`;
- `TB_RELATORIO_PRIORIDADE`.

Para preparar um schema novo, execute os arquivos nesta ordem:

1. `bd_motiva_criacao.sql` - cria as tabelas, chaves e restrições;
2. `bd_motiva_dados.sql` - inclui os dados iniciais usados na demonstração.

O arquivo de criação não possui comandos `DROP`. Caso as tabelas já existam, não execute novamente esse arquivo.

## Configuração da conexão

A aplicação lê os dados da conexão pelas seguintes variáveis de ambiente:

```powershell
$env:MOTIVA_DB_URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
$env:MOTIVA_DB_USER = "SEU_RM"
$env:MOTIVA_DB_PASSWORD = "SUA_SENHA"
```

Assim, o usuário e a senha permanecem fora do código-fonte e do repositório.

## Compilação

Com o Java 17 configurado no computador, execute no PowerShell:

```powershell
java -version
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

## Execução

Depois de configurar as variáveis de ambiente e preparar o banco:

```powershell
java -cp "out;lib\ojdbc17.jar" br.com.motiva.main.Main
```

Durante a execução, o `Main`:

1. abre a conexão com o Oracle;
2. demonstra o CRUD de equipe de manutenção;
3. demonstra o CRUD de trecho de rodovia;
4. demonstra o CRUD de intervenção operacional;
5. demonstra o CRUD de relatório;
6. gera e salva um novo relatório de prioridades;
7. consulta o histórico de relatórios;
8. remove os registros temporários da demonstração;
9. fecha a conexão.

## Tratamento de erros

- configurações ausentes são informadas pela `ConexaoBD`;
- erros do Oracle são exibidos no console;
- os valores das consultas são enviados por parâmetros;
- a conexão é fechada no bloco `finally` do `Main`;
- as chaves estrangeiras protegem os relacionamentos entre os registros.

## Resultados da validação

Validação realizada com Java 17 e Oracle FIAP em 21/09/2026.

| Verificação | Resultado |
|---|---|
| Compilação de todos os fontes | Aprovada |
| Compilação com `-Xlint:all` | Aprovada, sem avisos |
| Regras do motor de prioridade | 8 de 8 cenários aprovados |
| Cálculo do resumo do relatório | Aprovado |
| Criação das tabelas no Oracle | Aprovada |
| Inclusão dos dados iniciais | Aprovada |
| CRUD dos quatro DAOs | Aprovado |
| Persistência e consulta do relatório | Aprovadas |

Após a execução completa, o banco permaneceu com 2 equipes, 4 trechos, 3 intervenções e 2 relatórios registrados.
