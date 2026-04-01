# CLAUDE.md

Este documento é a referência central do projeto **One Pace App**. Contém os padrões de projeto, convenções de código, arquitetura e o **Processo de Desenvolvimento** que deve ser seguido em toda atividade (feature, fix, refactoring, etc.).

---

## Visão Geral

**One Pace App** — Aplicativo de streaming exclusivo da série One Piece, desenvolvido em Kotlin com Jetpack Compose. Projeto pessoal, sem bibliotecas ou serviços pagos.

**API de vídeos:** `https://superflixapi.rest/doc`

---

## Stack Tecnológica

| Tecnologia | Uso |
|---|---|
| **Kotlin** | Linguagem principal |
| **Jetpack Compose** | UI declarativa |
| **MVVM** | Arquitetura de apresentação |
| **Hilt** | Injeção de dependência |
| **Ktor Client** | Requisições HTTP |
| **Kotlinx Serialization** | Serialização/deserialização JSON |
| **Media3 (ExoPlayer)** | Player de vídeo/streaming |
| **Jetpack Navigation Compose** | Navegação entre telas |
| **Coil** | Carregamento de imagens |
| **Room** | Persistência local (favoritos, progresso, cache) |
| **DataStore** | Preferências do usuário |

> **Regra:** Todas as dependências devem ser gratuitas e open-source. Nenhuma biblioteca ou serviço pago é permitido.

---

## Build & Run Commands

```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Instalar no dispositivo conectado
./gradlew installDebug

# Limpar build
./gradlew clean

# Sync dependências
./gradlew --refresh-dependencies
```

---

## Arquitetura

### MVVM + Clean Architecture por Feature

```
app/
├── src/main/java/com/onepace/app/
│   ├── core/                    # Código compartilhado entre features
│   │   ├── di/                  # Módulos Hilt globais
│   │   ├── network/             # Configuração Ktor, interceptors
│   │   ├── player/              # Configuração ExoPlayer
│   │   ├── ui/                  # Componentes Compose reutilizáveis (theme, components)
│   │   ├── util/                # Extensions e utilitários
│   │   └── data/                # Models e DTOs compartilhados
│   │
│   ├── features/                # Features organizadas por domínio
│   │   ├── home/                # Tela inicial
│   │   │   ├── data/            # Repository, data sources, DTOs
│   │   │   ├── domain/          # Models de domínio, use cases (se necessário)
│   │   │   └── presentation/    # Screen, ViewModel, UI state, components
│   │   │
│   │   ├── player/              # Player de vídeo
│   │   │   ├── data/
│   │   │   ├── domain/
│   │   │   └── presentation/
│   │   │
│   │   ├── search/              # Busca de episódios/arcos
│   │   │   ├── data/
│   │   │   ├── domain/
│   │   │   └── presentation/
│   │   │
│   │   └── ...                  # Outras features
│   │
│   ├── navigation/              # NavHost, rotas, grafo de navegação
│   └── OnePaceApp.kt            # Application class (@HiltAndroidApp)
│
├── src/main/res/                # Resources (ícones, strings, etc.)
└── build.gradle.kts             # Dependências e configurações
```

### Fluxo de Dependência (MVVM)

```
Screen (Compose) → ViewModel → Repository → Data Source (API / Local)
         ↑                          ↓
      UI State                  Ktor Client / Room DB
```

- **Screen:** Composable functions que observam o estado do ViewModel.
- **ViewModel:** Gerencia UI state, processa eventos do usuário, chama repositories.
- **Repository:** Abstrai a origem dos dados (remoto vs local). Single source of truth.
- **Data Source:** Acesso direto à API (Ktor) ou banco local (Room).

### Regras de Camada

| Camada | Pode acessar | Não pode acessar |
|---|---|---|
| **Screen (Composable)** | ViewModel (via `hiltViewModel()`) | Repository, DataSource, Ktor |
| **ViewModel** | Repository, Use Cases | DataSource diretamente, Context do Android |
| **Repository** | Data Sources (Remote + Local) | ViewModel, Screen |
| **Data Source** | Ktor Client, Room DAO | Repository, ViewModel |

---

## Convenções de Código

### Nomenclatura

| Elemento | Convenção | Exemplo |
|---|---|---|
| Packages | lowercase, por feature | `features.home.presentation` |
| Classes | PascalCase com sufixo | `HomeScreen`, `HomeViewModel`, `EpisodeRepository` |
| Composables | PascalCase (sem sufixo `Composable`) | `EpisodeCard`, `PlayerControls` |
| Funções | camelCase | `fetchEpisodes()`, `playVideo()` |
| State classes | PascalCase + `UiState` | `HomeUiState`, `PlayerUiState` |
| Events/Actions | PascalCase + `Event` | `HomeEvent.OnEpisodeClick` |
| Constants | UPPER_SNAKE_CASE | `BASE_URL`, `PLAYER_TIMEOUT` |
| Resources (strings) | snake_case | `episode_title`, `error_network` |

### Estrutura de ViewModel

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnEpisodeClick -> navigateToPlayer(event.episodeId)
            is HomeEvent.OnRefresh -> loadEpisodes()
        }
    }
}
```

**Regras:**
- UI State como `data class` com `StateFlow`.
- Eventos do usuário via sealed class/interface.
- Injeção de dependência via construtor com `@Inject`.
- Nunca expor `MutableStateFlow` — apenas `StateFlow`.

### Estrutura de Screen

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayer: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToPlayer = onNavigateToPlayer
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToPlayer: (String) -> Unit
) {
    // UI implementation
}
```

**Regras:**
- Separar Screen (com ViewModel) de Content (stateless, previewable).
- Navegação via lambdas, nunca acessando NavController diretamente no composable.
- Usar `collectAsStateWithLifecycle()` para coletar flows.

### Networking (Ktor)

```kotlin
// Data Source
class EpisodeRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun fetchEpisodes(): List<EpisodeDto> {
        return client.get("endpoint").body()
    }
}
```

**Regras:**
- Um `HttpClient` configurado globalmente via módulo Hilt.
- DTOs separados dos models de domínio — mapear com extension functions.
- Tratar erros de rede no Repository, não no DataSource.

### Injeção de Dependência (Hilt)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}
```

**Regras:**
- Módulos Hilt em `core/di/`.
- `@Singleton` para clients HTTP, banco de dados.
- `@ViewModelScoped` para repositórios se necessário.
- Nunca instanciar dependências com `new`/construtor direto dentro de ViewModels.

### Tratamento de Erros

```kotlin
// Result wrapper
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
```

**Regras:**
- Usar sealed class `Resource<T>` para representar estados de operações assíncronas.
- Repository retorna `Flow<Resource<T>>` ou `Resource<T>`.
- ViewModel mapeia `Resource` para `UiState`.
- Nunca usar `try/catch` genérico em Composables — tratar no ViewModel.
- Exibir mensagens de erro amigáveis ao usuário (nunca stack traces).

### Player (ExoPlayer/Media3)

**Regras:**
- Configuração do ExoPlayer encapsulada em `core/player/`.
- Gerenciar ciclo de vida do player no ViewModel ou via `DisposableEffect`.
- Liberar recursos do player ao sair da tela.
- Suportar controles: play/pause, seek, fullscreen, velocidade.
- Salvar progresso de reprodução localmente (Room/DataStore).

### Geral

- **Sem código morto** — remover imports, variáveis e funções não utilizadas.
- **Sem comentários desnecessários** — código deve ser auto-explicativo.
- **Strings da UI** em `strings.xml` — nunca hardcoded em Composables.
- **Temas e cores** centralizados em `core/ui/theme/`.
- **Dimensões e espaçamentos** consistentes via sistema de design.
- **Sem bibliotecas pagas** — apenas dependências open-source/gratuitas.

---

## Processo de Desenvolvimento

O processo de desenvolvimento segue **6 fases sequenciais obrigatórias**. Cada fase possui um objetivo claro e critérios de entrada/saída.

> **REGRA FUNDAMENTAL:** Todas as 6 fases DEVEM ser executadas sequencialmente. Nenhuma fase pode ser pulada, reordenada ou mesclada com outra. A única exceção é quando uma fase é comprovadamente desnecessária para a task em questão — nesse caso, deve-se justificar formalmente por que a fase não se aplica antes de avançar.
>
> Exemplos de exceções válidas:
> - **Fase 3 (Planejamento):** Pode ser simplificada para fixes triviais de 1-2 linhas, mas ainda deve ser executada.
>
> Nunca é válido pular fases por "economia de tempo" ou porque "a mudança é pequena".

### Fase 1: Descoberta

**Objetivo:** Receber e compreender a task (feature, fix, refactoring, etc.).

**Atividades:**
- Receber a descrição da task do usuário.
- Identificar o tipo de atividade: `feature`, `fix`, `refactoring`, `chore`.
- Esclarecer dúvidas e ambiguidades com o usuário antes de prosseguir.
- Identificar requisitos funcionais e não-funcionais.
- Mapear dependências externas (APIs, bibliotecas, permissões Android).

**Critérios de saída:** Task compreendida com clareza, tipo identificado, requisitos mapeados.

---

### Fase 2: Análise

**Objetivo:** Analisar o código existente, identificar pontos de impacto e determinar como adequar a mudança à arquitetura MVVM do projeto.

**Atividades:**
- Ler e compreender os arquivos que serão afetados (screens, viewmodels, repositories, data sources).
- Identificar **todos** os pontos de impacto: telas, viewmodels, repositories, models, navegação.
- Verificar se existem padrões similares já implementados no projeto (reutilizar padrões existentes).
- Mapear dependências entre camadas que serão afetadas.
- Identificar se será necessário: novos módulos Hilt, novas entidades Room, novas rotas de navegação.
- Verificar impacto no grafo de navegação.

**Critérios de saída:** Mapa completo de impacto documentado, arquivos afetados listados, dependências identificadas.

---

### Fase 3: Planejamento

**Objetivo:** Criar um plano de implementação detalhado, considerando trade-offs e compatibilidade com a arquitetura.

**Atividades:**
- Definir a ordem de implementação (bottom-up: model → data source → repository → viewmodel → screen).
- Listar todas as alterações necessárias arquivo por arquivo.
- Considerar trade-offs e justificar decisões técnicas.
- Planejar tratamento de erros adequado.
- Definir se novos módulos Hilt são necessários.
- Planejar novas rotas de navegação (se aplicável).

**Critérios de saída:** Plano aprovado pelo usuário com lista de alterações, ordem de execução e justificativas.

---

### Fase 4: Implementação

**Objetivo:** Implementar o plano seguindo as melhores práticas, convenções do projeto e máxima clareza de código.

**Atividades:**
- Implementar na ordem definida no plano (geralmente bottom-up).
- Seguir rigorosamente todas as convenções de código documentadas neste arquivo.
- Cada arquivo criado ou alterado deve:
  - Respeitar a nomenclatura do projeto.
  - Usar injeção via construtor com Hilt.
  - Usar o tratamento de erros via `Resource<T>`.
  - Manter separação de camadas (MVVM).
  - Strings da UI em `strings.xml`.
- DTOs separados dos models de domínio.
- Não introduzir over-engineering: implementar exatamente o que foi planejado.

**Regras de qualidade:**
- Código limpo, sem comentários desnecessários.
- Sem código morto (dead code).
- Sem imports não utilizados.
- Sem variáveis não utilizadas.
- Funções curtas e com responsabilidade única.
- Nomes auto-explicativos.
- Composables stateless quando possível (separar Screen de Content).

**Critérios de saída:** Código implementado, compilando sem erros (`./gradlew assembleDebug`).

---

### Fase 5: Revisão de Código

**Objetivo:** Analisar todo o código gerado e validar conformidade com os padrões do projeto.

**Checklist de revisão:**
- [ ] Nomenclatura segue as convenções (classes, funções, packages).
- [ ] Injeção de dependência via construtor com `@Inject` (sem instanciação manual).
- [ ] Tratamento de erros usa `Resource<T>` (sem try/catch genérico em composables).
- [ ] Separação de camadas respeitada (Screen não acessa Repository, ViewModel não acessa Context).
- [ ] UI State como `StateFlow` (nunca `MutableStateFlow` exposto).
- [ ] Composables separados em Screen (stateful) e Content (stateless).
- [ ] Navegação via lambdas (sem NavController direto em composables).
- [ ] Strings em `strings.xml` (sem hardcode).
- [ ] Sem código morto, imports não usados ou variáveis soltas.
- [ ] Sem bibliotecas pagas.
- [ ] Player libera recursos corretamente ao sair da tela.

**Critérios de saída:** Código 100% em conformidade com os padrões. Correções aplicadas se necessário.

---

### Fase 6: Git

**Objetivo:** Gerar a mensagem de commit padronizada. **O commit NÃO deve ser executado — apenas a mensagem deve ser gerada e exibida.**

**Template de commit:**
```
{tipo} - {Descrição resumida da alteração}
- {Detalhe 1 da alteração}
- {Detalhe 2 da alteração}
- {Detalhe N da alteração}
```

**Tipos válidos:**

| Tipo | Uso |
|---|---|
| `feature` | Nova funcionalidade |
| `fix` | Correção de bug |
| `refactor` | Refatoração sem mudança de comportamento |
| `chore` | Tarefas de manutenção (deps, configs) |

**Exemplo:**
```
feature - Implementação da tela de listagem de episódios
- Criado EpisodeRepository com integração à API SuperFlix
- Criado HomeViewModel com UI State e eventos
- Criada HomeScreen com grid de episódios e pull-to-refresh
- Criado EpisodeCard composable reutilizável
- Adicionada rota home no NavHost
- Configurado módulo Hilt para injeção do HttpClient
```

**Regras:**
- O commit **nunca** deve ser executado automaticamente — apenas a mensagem é gerada e exibida.
- A mensagem deve refletir **todas** as alterações feitas, não apenas as principais.
- Cada item deve ser conciso mas descritivo.

**Critérios de saída:** Mensagem de commit exibida ao usuário para revisão e execução manual.

---

## Resumo do Processo

```
1. Descoberta    → Compreender a task
2. Análise       → Mapear impacto no código
3. Planejamento  → Definir plano de implementação
4. Implementação → Codificar seguindo padrões
5. Revisão       → Verificar conformidade
6. Git           → Gerar mensagem de commit (não executar)
```

---

## Agentes de Desenvolvimento

### Agentes de Fase (Processo)

Cada fase do processo possui um agente Claude Code dedicado. Os agentes são acionados conforme a fase.

| Fase | Agente | Modelo | Justificativa |
|---|---|---|---|
| 1. Descoberta | `01-discovery` | Sonnet | Compreensão rápida, perguntas objetivas |
| 2. Análise | `02-analysis` | Sonnet | Leitura e mapeamento de código existente |
| 3. Planejamento | `03-planning` | Opus | Decisões arquiteturais, trade-offs complexos |
| 4. Implementação | `04-implementation` | Opus | Geração de código de alta qualidade |
| 5. Revisão de Código | `05-code-review` | Opus | Análise crítica e detecção de violações |
| 6. Git | `06-git` | Sonnet | Geração de mensagem de commit |

### Agentes Especialistas

| Agente | Modelo | Responsabilidade |
|---|---|---|
| `design` | Opus | Especialista UX/UI. Projeta telas, cria componentes Compose, aplica e evolui o Design System (`docs/DESIGN_SYSTEM.md`). Pesquisa na internet antes de decisões. Justifica escolhas com fundamentos técnicos (WCAG, Nielsen, Fitts, etc.). |

**Quando acionar o agente `design`:**
- Quando a task envolve criação ou alteração de telas.
- Quando novos componentes visuais precisam ser criados.
- Quando decisões de UX/UI precisam ser tomadas.
- O agente de design é acionado **durante a Fase 4 (Implementação)** para tudo que envolve UI.
- O agente de design **sempre** atualiza `docs/DESIGN_SYSTEM.md` após criar novos componentes ou padrões.

### Fluxo de Acionamento

```
Task recebida
  │
  ▼
01-discovery (compreende a task, esclarece dúvidas)
  │
  ▼
02-analysis (analisa código, mapeia impacto)
  │
  ▼
03-planning (cria plano, aguarda aprovação do usuário)
  │
  ▼
04-implementation (implementa seguindo padrões, valida compilação)
  │  ├── design (acionado para tasks com UI — projeta e implementa telas/componentes)
  │
  ▼
05-code-review (valida conformidade com checklist, corrige violações)
  │
  ▼
06-git (analisa alterações, gera mensagem de commit — NÃO executa)
  │
  ▼
Mensagem de commit exibida ao usuário
```

### Uso dos Agentes

**Exemplos de acionamento:**
- "Quero implementar X" → Inicia com `01-discovery`
- "Analise o impacto de Y" → Aciona `02-analysis`
- "Planeje a implementação" → Aciona `03-planning`
- "Implemente o plano" → Aciona `04-implementation`
- "Crie a tela de X" → Aciona `design` (via implementação)
- "Revise o código" → Aciona `05-code-review`
- "Gere o commit" → Aciona `06-git`
