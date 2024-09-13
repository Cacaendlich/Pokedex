# Pokédex - Desafio de Estágio

Este projeto é uma Pokédex desenvolvida em Kotlin e Android como parte de um desafio no meu estágio.
O principal objetivo foi desenvolver minhas habilidades no desenvolvimento mobile Android, seguindo boas práticas de arquitetura e desenvolvimento.

## Sobre o Projeto

A Pokédex é uma aplicação móvel para listar, favoritar, visualizar detalhes e comparar Pokémons, utilizando a [PokéAPI](https://pokeapi.co/).
O projeto foi dividido em duas etapas principais, onde a primeira focou no uso de XML e a segunda na refatoração com **Jetpack Compose**.

## Funcionalidades

- **Salvar Preferências de Login**: Utiliza **Shared Preferences** para salvar as preferências do usuário após o login.
- **Listagem de Todos os Pokémons**: Exibe uma lista de todos os Pokémons consumindo os dados da [PokéAPI](https://pokeapi.co/).
- **Favoritar e Desfavoritar Pokémons**: O usuário pode marcar ou desmarcar um Pokémon como favorito.
- **Persistência Local de Favoritos**: Pokémons favoritos são salvos no banco de dados local utilizando **Room**.
- **Lista de Pokémons Favoritos**: Exibe apenas os Pokémons marcados como favoritos.
- **Exibição de Detalhes Completos**: Mostra informações detalhadas sobre cada Pokémon, incluindo suas estatísticas e habilidades.
- **Comparar Características de Dois Pokémons**: Nova funcionalidade que permite comparar dois Pokémons, exibindo suas características lado a lado:
  
  **Base de comparação**
  - **HP**: `it.base_stat * 1.0`
  - **Attack**: `it.base_stat * 1.5`
  - **Defense**: `it.base_stat * 1.2`
  - **Speed**: `it.base_stat * 1.4`

## Demonstração

Tela de Login:

![Login view](https://github.com/Cacaendlich/Pokedex/blob/main/Captura%20de%20Tela%202024-09-12%20a%CC%80s%2023.11.29.png)

Tela de Lista de Pokemons:

https://github.com/user-attachments/assets/b0ff8b57-be57-4dc8-8e39-60737272881b

Tela de Detalhes do Pokemon:

https://github.com/user-attachments/assets/3f99d9be-2dff-4dd7-be54-f514e8c6392d

Tela de Comparação de 2 Pokemons:

https://github.com/user-attachments/assets/cc6d05fb-cade-4e6a-8a84-aedd2440c31f

https://github.com/user-attachments/assets/1822ca0c-eede-49a1-889f-2ae5e8fb5bb8


## Tecnologias Utilizadas

### Primeira Etapa
- **Kotlin**
- **XML** para criação das interfaces.
- **Shared Preferences** para salvar preferências do usuário.
- **Retrofit** para consumo da [PokéAPI](https://pokeapi.co/).
- **RecyclerView** para exibição de listas.
- **LiveData** e **ViewModel** para arquitetura reativa.
- **Room** para persistência local dos Pokémons favoritos.
- **Mockito** para testes unitários.
- **TDD** e **BDD** como metodologias de desenvolvimento.

### Segunda Etapa
- **Jetpack Compose**: Refatoração completa das interfaces, migrando do XML para uma abordagem declarativa com Compose.
- **Coroutines**: Uso de coroutines para melhorar o gerenciamento de concorrência e chamadas assíncronas.
- **Tela de Comparação**: Tela desenvolvida para comparação direta de Pokémons.

## Instalação

### Pré-requisitos
- Android Studio (versão mais recente).
- Acesso à [PokéAPI](https://pokeapi.co/).

### Para instalação

**Clone o repositório:**

   Abra o terminal e execute o seguinte comando:
   ```bash
   git clone https://github.com/Cacaendlich/Pokedex.git
