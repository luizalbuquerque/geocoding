# Projeto Geocoding API Service

Este projeto tem como objetivo fornecer um serviço de geocodificação que traduz coordenadas (latitude e longitude) em endereços legíveis e os armazena em um banco de dados para rastreamento e auditoria.

## Stack Tecnológica

- [x] Java 17
- [x] Spring Boot 3.1.3
- [x] RestTemplate
- [x] H2 Database (em memória)
- [x] Maven (gestão de dependências)
- [x] Photon Komoot para serviço de geocodificação
- [x] Swagger (documentação da API)
- [x] JUnit5 (Testes unitários)

## Como executar

1. Certifique-se de ter o Java e o Maven instalados.
2. Clone este repositório para sua máquina local.
3. Navegue até o diretório do projeto via terminal.
4. Execute o seguinte comando para compilar e iniciar o projeto:
5. A aplicação estará rodando no endereço https://localhost:8080

   ```bash
   mvn spring-boot:run
   ```
   Para acessar Swagger (documentação da API)
  
    ```bash
   http://localhost:8080/swagger-ui.html
    ```

## Sobre o projeto

Este projeto foi desenvolvido como parte de uma avaliação para demonstrar competências em desenvolvimento Java, organização, documentação e qualidade de código. Utiliza-se a API de geocodificação da Photon Komoot em substituição à API Geocoding do Google.

### Premissas

- A API recebe requisições com parâmetros de latitude e longitude.
- A resposta é o endereço completo correspondente às coordenadas fornecidas.
- Cada consulta é armazenada no banco de dados com detalhes como parâmetros, data/hora da operação e a resposta em formato JSON.

### Recursos adicionais

- [ ] Autenticação simples (a implementar, se necessário).
- [x] Testes unitários para validar a lógica do negócio e integrações.
- [ ] Frontend básico para consulta (a implementar, se necessário).

### Fontes e referências

- [Photon Komoot API](https://photon.komoot.io/)
- [Artigo de referência](https://www.devmedia.com.br/como-utilizar-a-google-geocoding-api-para-obter-enderecos/36751#modulo-mvp)
