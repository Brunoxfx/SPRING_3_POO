# MOTIVA — Sprint 3 de Programacao Orientada a Objetos

Sistema Java puro para monitorar a vegetacao em trechos de rodovia, definir prioridades de intervencao e persistir os dados e o historico de relatorios em um banco Oracle por meio de JDBC.

Esta entrega evolui a Sprint 2: as regras de prioridade, a heranca das intervencoes, o polimorfismo e o monitoramento IoT foram preservados. A Sprint 3 acrescenta conexao com banco, scripts SQL, records de persistencia e quatro DAOs com CRUD completo.

## Equipe

| Nome | RM |
|---|---|
| Bruno Anselmo da Silva | RM 566521 |
| Fernando de Almeida Godoi | RM 564820 |
| Gabriel Ber Soares | RM 563520 |
| Guilherme de Freitas Salgado | RM 562494 |
| Vinicius Ribeiro Dias | RM 566468 |

## Requisitos atendidos

- Java 17 e JDBC puro, sem Spring ou JPA.
- Driver Oracle `ojdbc17.jar` na pasta `lib`.
- Carregamento explicito de `oracle.jdbc.driver.OracleDriver`, conforme a referencia da atividade.
- Scripts próprios para criação das tabelas e inclusão de dados de teste.
- `ConexaoBD` no padrão Singleton.
- `EquipeManutencaoDAO`, `TrechoRodoviaDAO`, `IntervencaoOperacionalDAO` e `RelatorioPrioridadeDAO`.
- Todos os DAOs possuem `inserir`, `buscarPorId`, `listarTodas`, `atualizar` e `deletar`.
- Consultas parametrizadas com `PreparedStatement` e fechamento automático de `PreparedStatement` e `ResultSet`.
- Records para representar os registros persistidos.
- Relatório exibido no console e salvo no Oracle.
- `Main` demonstrando conexão, CRUD dos quatro DAOs e consulta do histórico.
- Regras da Sprint 2 e das quatro faixas de prioridade validadas antes da entrega.

## Regras de prioridade

| Condição | Prioridade | Intervenção |
|---|---|---|
| Vegetação com 80 cm ou mais e risco alto | URGENTE | Roçada manual |
| Vegetação com 80 cm ou mais e risco baixo | CRÍTICO | Roçada mecanizada |
| Vegetação entre 40 cm e 79,9 cm | ATENÇÃO | Pulverização |
| Vegetação abaixo de 40 cm | NORMAL | Sem intervenção |

## Estrutura

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
|-- seu-script-criacao.sql
|-- seu-script-dados.sql
`-- README.md
```

## Banco de dados

Execute no Oracle, nesta ordem:

1. `seu-script-criacao.sql` — cria as quatro tabelas, chaves, relacionamentos e restrições;
2. `seu-script-dados.sql` — insere equipes, quatro trechos, intervenções e um relatório inicial.

O script de criação deve ser executado uma única vez em um schema vazio. Ele não contém comandos `DROP`, evitando apagar tabelas ou dados por acidente.

## Configuração segura da conexão

As credenciais não ficam no código. No PowerShell usado para executar o sistema, configure:

```powershell
$env:MOTIVA_DB_URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
$env:MOTIVA_DB_USER = "SEU_RM"
$env:MOTIVA_DB_PASSWORD = "SUA_SENHA"
```

Troque somente os valores na sua sessão local. Não grave a senha no README, no código, em prints ou no Git.

## Compilação

O projeto usa records e deve ser compilado com Java 17 ou superior. No PowerShell, dentro da pasta do projeto:

```powershell
$env:JAVA_HOME = "C:\Users\ansel\.jdks\ms-17.0.18"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

As classes usam somente a API padrão `java.sql` durante a compilação. O driver é incluído no classpath na execução, quando a conexão Oracle é aberta.

## Validacao realizada antes da entrega

- compilacao de todos os fontes com Java 17;
- compilacao com `-Xlint:all`, sem avisos;
- oito verificacoes do motor de prioridade aprovadas;
- verificacao do resumo com uma ocorrencia de cada prioridade aprovada;
- carregamento do driver Oracle confirmado;
- scripts de criacao e dados executados no Oracle FIAP;
- CRUD dos quatro DAOs e persistencia do historico executados com sucesso.

Os arquivos auxiliares usados nessa validacao nao fazem parte do repositorio final, mantendo somente os entregaveis solicitados pela atividade.

## Execução completa com Oracle

Depois de executar os dois scripts e configurar as três variáveis de ambiente:

```powershell
java -cp "out;lib\ojdbc17.jar" br.com.motiva.main.Main
```

O `Main`:

1. abre a conexão;
2. demonstra inserir, buscar, listar e atualizar uma equipe;
3. demonstra o CRUD de trecho e intervenção;
4. demonstra o CRUD de relatório com um registro temporário;
5. gera o relatório com todos os trechos e salva o resultado;
6. consulta o histórico;
7. remove apenas os registros temporários criados pela demonstração;
8. mantém o relatório gerado no histórico e fecha a conexão.

## Tratamento de erros

- Se alguma variável de ambiente estiver ausente, `ConexaoBD` informa exatamente qual configuração falta.
- Se o driver não estiver no classpath, a aplicação informa que `lib/ojdbc17.jar` deve ser adicionado.
- Erros SQL são apresentados no console com a mensagem do Oracle.
- `PreparedStatement` impede a concatenação direta de valores nas consultas.
- Os recursos JDBC são fechados com `try-with-resources`; a conexão compartilhada é fechada no `finally` do `Main`.

## Situação da validação

- Compilação com Java 17: executada localmente.
- Testes das regras sem banco: 8 de 8 testes do motor e 1 de 1 teste do resumo aprovados.
- Execução no Oracle FIAP: aprovada em 21/09/2026.
- Estado confirmado depois do teste: 2 equipes, 4 trechos, 3 intervencoes e 2 relatorios persistidos.
- O relatório mais recente registrou 2 urgentes, 1 crítico, 1 atenção e 1 normal durante a demonstração do CRUD.
