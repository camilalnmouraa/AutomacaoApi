import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserTests {

    // Constantes
    private static final String BASE_URI = "https://reqres.in/api";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final int STATUS_CODE_BAD_REQUEST = 400;
    private static final String ERROR_MESSAGE_MISSING_PASSWORD = "Missing password";
    private static final String ERROR_MESSAGE_DEFINED_USERS = "Note: Only defined users succeed registration";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void testExistingUser() {
        // Criação do corpo da requisição
        String requestBody = buildUserRequestBody("Emma Wong", "emma.wong@reqres.in", "password123");
        // Execução da requisição e validação da resposta
        assertCommonResponses(registerUser(requestBody), "Usuário já existente");
    }

    @Test
    public void testUnauthorizedUser() {
        // Criação do corpo da requisição
        String requestBody = buildUserRequestBody("Unauthorized User", "unauthorized.user@reqres.in", "password123");
        // Execução da requisição e validação da resposta
        assertCommonResponses(registerUser(requestBody), "Usuário sem autorização");
    }

    @Test
    public void testBlankPasswordUser() {
        // Criação do corpo da requisição
        String requestBody = buildUserRequestBody("Blank Password User", "blank.password@reqres.in", "");
        // Execução da requisição e validação da resposta
        assertCommonResponsesWithSpecificError(registerUser(requestBody), "Usuário com senha em branco", ERROR_MESSAGE_MISSING_PASSWORD);
    }

    @Test
    public void testNoEmailUser() {
        // Criação do corpo da requisição
        String requestBody = buildUserRequestBody("No Email User", "", "password123");
        // Execução da requisição e validação da resposta
        assertCommonResponsesWithSpecificError(registerUser(requestBody), "Usuário sem email", ERROR_MESSAGE_DEFINED_USERS);
    }

    private String buildUserRequestBody(String username, String email, String password) {
        // Corpo da requisição em JSON
        return String.format("{ \"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\" }", username, email, password);
    }

    private Response registerUser(String requestBody) {
        // Envio da requisição POST para o endpoint /register
        return given()
                .header("Content-Type", CONTENT_TYPE_JSON)
                .body(requestBody)
                .when()
                .post("/register");
    }
    private void assertBadRequestResponse(Response response, String testName) {
        // Assertiva: Código de status esperado para uma resposta de requisição inválida
        assertEquals(STATUS_CODE_BAD_REQUEST, response.getStatusCode(),
                "Código de status incorreto. Esperava 400. Teste: " + testName);
        System.out.println("Resposta para " + testName + ": " + response.getBody().asString());
    }

    private void assertCommonResponses(Response response, String testName) {
        // Validação comum para todas as respostas
        assertBadRequestResponse(response, testName);
        assertContentType(response, CONTENT_TYPE_JSON);
        assertContentLengthPresent(response);
        assertServerHeaderPresent(response);
        assertNonEmptyResponseBody(response);
    }
    private void assertCommonResponsesWithSpecificError(Response response, String testName, String expectedError) {
        // Validação comum com mensagem de erro específica
        assertCommonResponses(response, testName);
        //Verifica se a mensagem de erro na resposta coincide com a esperada
        assertNotNull(response.jsonPath().getString("error"), "Mensagem de erro ausente na resposta");
        assertEquals(expectedError, response.jsonPath().getString("error"),
                "Mensagem incorreta para " + testName);
    }

    private void assertContentType(Response response, String expectedContentType) {
        //Verifica se o tipo de conteúdo na resposta é o esperado
        String actualContentType = response.getContentType();
        assertTrue(actualContentType.toLowerCase().contains(expectedContentType.toLowerCase()),
                "Content-Type incorreto. Esperado: " + expectedContentType + ", Retornado: " + actualContentType);
        System.out.println("Assertiva 'assertContentType' passou. Content-Type: " + actualContentType);
    }

    private void assertContentLengthPresent(Response response) {
        //Verifica se o cabeçalho Content-Length está presente e contém um valor numérico
        assertNotNull(response.getHeader("Content-Length"), "Cabeçalho Content-Length ausente");
        assertTrue(response.getHeader("Content-Length").matches("\\d+"), "O valor do cabeçalho Content-Length não é um número");
    }

    private void assertServerHeaderPresent(Response response) {
        //Verifica se o cabeçalho Server está presente
        assertNotNull(response.getHeader("Server"), "Cabeçalho Server ausente");
    }

    private void assertNonEmptyResponseBody(Response response) {
        //Verifica se o corpo da resposta não está vazio
        assertNotNull(response.getBody().asString(), "O corpo da resposta está vazio");
    }
}