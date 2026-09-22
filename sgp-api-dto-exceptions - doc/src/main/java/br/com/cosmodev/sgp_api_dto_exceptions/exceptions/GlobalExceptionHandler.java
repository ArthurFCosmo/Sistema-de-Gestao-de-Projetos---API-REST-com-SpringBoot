package br.com.cosmodev.sgp_api_dto_exceptions.exceptions;

import br.com.cosmodev.sgp_api_dto_exceptions.dtos.ErroPadraoResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElementoNaoEncontradoException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<ErroPadraoResponseDTO> tratarElementoNaoEncontradoException(HttpServletRequest request, ElementoNaoEncontradoException exception) {
        return criarExceptionPadrao(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioVioladaException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<ErroPadraoResponseDTO> tratarRegraDeNegocioVioladaException(HttpServletRequest request, RegraDeNegocioVioladaException exception) {
        return criarExceptionPadrao(request, HttpStatus.UNPROCESSABLE_CONTENT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<ErroPadraoResponseDTO> tratarMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {

        String mensagemFormatada = exception.getBindingResult().getFieldErrors().stream().map(
                e -> e.getField() + ": " + e.getDefaultMessage()
        ).collect(Collectors.joining(", ")
        );

        return criarExceptionPadrao(request, HttpStatus.BAD_REQUEST, mensagemFormatada);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<ErroPadraoResponseDTO> tratarDataIntegrityViolationException(HttpServletRequest request, @SuppressWarnings("unused") DataIntegrityViolationException exception) {
        return criarExceptionPadrao(request, HttpStatus.CONFLICT, "Já existe um registro com esses dados (CPF/E-mail).");
    }


    // Catcha a exception lançada no service pela linha authenticationManager.authenticate() no service de login, e trata ela.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroPadraoResponseDTO> tratarAuthenticationException(HttpServletRequest request, AuthenticationException exception) {
        return criarExceptionPadrao(request, HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique e-mail e senha.");
    }

    // Catcha todas as exceptions lançadas pelo spring security nos endpoints quando um usuario tenta acessá-lo sem as roles adequadas.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroPadraoResponseDTO> tratarAccessDeniedException(HttpServletRequest request, AccessDeniedException exception) {
        return criarExceptionPadrao(request, HttpStatus.FORBIDDEN, "Acesso negado. Você não possui permissões para realizar essa operação.");
    }

    private ResponseEntity<ErroPadraoResponseDTO> criarExceptionPadrao(HttpServletRequest request, HttpStatus status, String mensagem) {

        ErroPadraoResponseDTO dtoException = new ErroPadraoResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURL().toString()
        );

        return ResponseEntity.status(status).body(dtoException);
    }
    
}
