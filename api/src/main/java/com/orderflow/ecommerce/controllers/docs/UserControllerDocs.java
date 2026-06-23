package com.orderflow.ecommerce.controllers.docs;

import com.orderflow.ecommerce.dtos.ErrorResponse;
import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "Endpoints para gerenciar User - CRUD de users")
public interface UserControllerDocs {

    @Operation(
            summary = "Obtém usuário por id",
            description = "Retorna um usuário com base no ID fornecido.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identificador numérico de usuário",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<UserResponse> findById(@PathVariable Long id);

    @Operation(
            summary = "Lista todas os usuários",
            description = "Retorna uma lista paginada de todos os usuários. Filtra por nome se o parâmetro 'name' for informado.",
            parameters = {
                    @Parameter(name = "name", description = "Filtro opcional por nome do usuário", required = false, example = "Maria"),
                    @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página", example = "10"),
                    @Parameter(name = "sort", description = "Campo e direção de ordenação", example = "name,asc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtida com sucesso",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    )
            }
    )
    ResponseEntity<Page<UserResponse>> findAll(Pageable pageable);

    @Operation(
            summary = "Obtém usuário por e-mail",
            description = "Retorna um usuário com base no e-mail fornecido.",
            parameters = {
                    @Parameter(
                            name = "email",
                            description = "E-mail do usuário",
                            required = true,
                            example = "maria@mail.com"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário inexistente",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<UserResponse> findByEmail(@RequestParam String email);

    @Operation(
            summary = "Insere um usuário",
            description = "Insere um novo usuário no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário cadastrado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Corpo inválido ou falha de validação",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest request);

    @Operation(
            summary = "Remove usuário por id",
            description = "Remove um usuário existente do sistema de forma permanente.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identificador número do usuário a remover",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuário removido com sucesso",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(
            summary = "Atualiza o dados de um usuário",
            description = "Atualiza os dados de um usuário existente com base no ID fornecido",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identificador numérico do usuário",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados para atualização do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Corpo inválido ou falha de validação nos dados enviados",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request);

}
