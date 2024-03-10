# AutomacaoApi

## Descrição
Este projeto consiste em testes de automação de API para o endpoint de registro de usuários (`/register`) na API ReqRes. Os testes foram implementados em Java utilizando a biblioteca RestAssured e o framework de teste JUnit 5.

## Pré-requisitos
1. Java 8 ou superior instalado
2. Maven instalado

## Configuração do Ambiente
Antes de executar os testes, certifique-se de configurar o ambiente:

1. **Clonar o Repositório:**
   ```bash
   git clone https://seu-repositorio/AutomacaoApi.git
   cd AutomacaoApi
   ```
2. **Configurar a Base URI:** <br>
Abra o arquivo RegisterUserTests.java localizado em src/test/java e modifique a constante BASE_URI com o endpoint da sua API.

3. **Executar os Testes**
   ```bash
    mvn clean test
   ```
## Estrutura do Projeto
- **`src/test/java/RegisterUserTests.java`:** Contém os casos de teste para o endpoint de registro de usuários.
- **`pom.xml`:** Arquivo de configuração do Maven que gerencia as dependências e plugins do projeto.

## Casos de Teste Implementados
1. **Usuário já existente (`testExistingUser`):**
   - Verifica se o registro de um usuário já existente retorna um código de status 400.
   
2. **Usuário sem autorização (`testUnauthorizedUser`):**
   - Verifica se o registro de um usuário sem autorização retorna um código de status 400.
   
3. **Usuário com senha em branco (`testBlankPasswordUser`):**
   - Verifica se o registro de um usuário com senha em branco retorna um código de status 400 e a mensagem de erro correta.
   
4. **Usuário sem e-mail (`testNoEmailUser`):**
   - Verifica se o registro de um usuário sem e-mail retorna um código de status 400 e a mensagem de erro correta.

## Assertivas Implementadas
- Verificação do código de status (esperado: 400).
- Verificação do tipo de conteúdo da resposta (esperado: "application/json").
- Verificação da presença do cabeçalho "Content-Length" e se contém um valor numérico.
- Verificação da presença do cabeçalho "Server".
- Verificação de que o corpo da resposta não está vazio.
- Verificação da mensagem de erro específica em casos específicos.

## Logs
Os logs das respostas são exibidos no console durante a execução dos testes.

## Melhorias Futuras
- Aumentar a cobertura de testes adicionando mais cenários de validação.
- Paralelizar a execução dos testes para aumentar a eficiência.
