package br.com.alura.screematch2.service;

public interface ICorverteDados {
    <T> T obterDados (String json, Class<T> classe);
    
}
