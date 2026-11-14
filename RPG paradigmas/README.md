# Crônicas do Abismo

RPG de exploração e combate por turnos executado no console, desenvolvido em Java 17. O projeto demonstra conceitos de programação orientada a objetos e princípios SOLID por meio de personagens, inimigos, itens, eventos e serviços separados por responsabilidade.

## Objetivo

Crie um herói, explore a masmorra, enfrente inimigos e chefes, colete itens e evolua. O jogo oferece três classes jogáveis e salva o progresso em JSON para continuar a aventura depois.

## Recursos do jogo

- **Guerreiro:** alta vida e defesa; seu Golpe Sísmico ignora parte da defesa do alvo.
- **Mago:** alto ataque; sua Explosão Arcana recebe bônus baseado em Sorte.
- **Arqueiro:** especialista em Sorte e Esquiva; sua habilidade combina ataque e Esquiva temporária.
- **Combate por turnos:** ataque básico, habilidade especial, uso de item e tentativa de fuga. Não é possível fugir de chefes.
- **Recarga da habilidade:** mais longa nos níveis iniciais e reduzida conforme o personagem evolui:
  - Níveis 1–5: 3 turnos.
  - Níveis 6–10: 2 turnos.
  - Nível 11 ou superior: 1 turno.
- **Progressão:** vitórias concedem XP e Ouro; subir de nível aumenta atributos e restaura a vida.
- **Chefes:** ficam disponíveis a cada três vitórias e têm uma habilidade de Fúria.
- **Eventos:** podem aparecer durante a exploração depois da fase inicial. Eventos de dano não reduzem a vida abaixo de 1 PV.
- **Itens e equipamentos:** poções, elixires, armas, armaduras e amuletos podem ser obtidos ou comprados. Monstros comuns têm chance base mínima de drop de 65%, aumentada pela Sorte; chefes sempre deixam um item.
- **Sorte:** influencia Esquiva, crítico, eventos e recompensas.
- **Persistência:** salva e carrega personagem, atributos, inventário, equipamentos e progresso em `savegame.json`.
- **Interface de console:** exibe os PV como valores numéricos (`atual/máximo`) e usa cores ANSI quando suportadas pelo terminal.

## Tecnologias

- Java 17
- Apache Maven
- [Gson](https://github.com/google/gson) para persistência JSON
- [Jansi](https://github.com/fusesource/jansi) para cores no console
- JUnit 5 para testes

## Pré-requisitos

- JDK 17 ou superior
- Apache Maven instalado e disponível no `PATH`

Confirme as instalações:

```shell
java -version
mvn -version
```

## Compilar, testar e executar

Na raiz do projeto:

```shell
# Compilar e executar os testes
mvn clean test

# Iniciar o jogo no console
mvn compile exec:java

# Criar o JAR executável
mvn clean package

# Executar o JAR gerado
java -jar target/rpg-console-solid-4.1.2.jar
```

Ao iniciar, escolha um novo jogo ou continue um progresso existente. As opções e ações são selecionadas digitando o número correspondente. O arquivo `savegame.json` é criado no diretório de execução ao salvar o jogo.

## Organização do código

| Pacote | Responsabilidade |
| --- | --- |
| `model` | Personagens, inimigos, atributos, raridades e resultados de ações. |
| `interfaces` | Contratos para atacar, esquivar, usar habilidades, usar itens, equipar e executar eventos. |
| `item` | Itens consumíveis, equipamentos e inventário. |
| `event` | Eventos de exploração e seu gerenciador. |
| `service` | Fluxo da aventura, combate, geração de loot e persistência. |
| `rng` | Abstração e implementação do gerador aleatório. |
| `ui` | Contrato de interface e implementação de console. |

O ponto de entrada e composição das dependências é `br.com.rpg.Main`.

## Princípios SOLID

| Princípio | Exemplos no projeto |
| --- | --- |
| **SRP — Responsabilidade Única** | `MotorCombate` orquestra turnos; `GeradorLoot` cria inimigos e recompensas; `PersistenciaService` cuida do save/load; `JogoService` coordena o fluxo da aventura. |
| **OCP — Aberto/Fechado** | Novos eventos implementam `EventoAleatorio` e podem ser registrados no `GerenciadorEventos`. |
| **LSP — Substituição de Liskov** | Personagens e inimigos especializados são usados através de `EntidadeCombate` e seus contratos. |
| **ISP — Segregação de Interface** | Capacidades são separadas em contratos pequenos, como `Atacante`, `Esquivavel`, `ItemUsavel` e `Equipavel`. |
| **DIP — Inversão de Dependência** | Serviços recebem abstrações como `GeradorAleatorio` e `InterfaceUsuario`, em vez de depender diretamente das implementações de console e aleatoriedade. |

## Testes

Execute toda a suíte com:

```shell
mvn test
```

Os testes cobrem regras de combate, inventário e persistência, dano de eventos e chance mínima de loot.

## Diagrama de classes

O diagrama PlantUML está em [`docs/diagrama-classes.puml`](docs/diagrama-classes.puml).
