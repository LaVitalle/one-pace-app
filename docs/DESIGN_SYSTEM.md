# Design System — One Pace App

Este documento define a identidade visual e o sistema de design do One Pace App. É um documento vivo, atualizado conforme novos componentes e padrões visuais são criados.

---

## 1. Identidade Visual

### Conceito

A identidade visual é inspirada na **bandeira dos Mugiwara (Chapéu de Palha)** — o símbolo icônico da tripulação do Luffy. A paleta de cores extrai as quatro cores fundamentais da bandeira: preto, branco, amarelo e vermelho.

O objetivo é criar uma interface **moderna, escura e imersiva**, que remeta ao universo pirata de One Piece sem ser temática demais — priorizando usabilidade, legibilidade e elegância visual.

### Diretrizes Visuais

- **Dark-first:** O app usa tema escuro como padrão. O preto e tons de cinza escuro formam a base.
- **Hierarquia por cor:** Amarelo para destaques e CTAs principais. Vermelho para estados de alerta, ações destrutivas e acentos secundários. Branco para texto e ícones primários.
- **Glassmorphism / Liquid Glass:** Aplicado com moderação em superfícies elevadas (cards, bottom sheets, modais) para criar profundidade. Nunca em elementos interativos pequenos (botões, chips) — apenas em containers.
- **Minimalismo funcional:** Cada elemento visual tem propósito. Sem decorações gratuitas.

---

## 2. Paleta de Cores

### Cores Primárias (Brand)

| Token | Hex | Uso |
|---|---|---|
| `brand_black` | `#0A0A0A` | Background principal |
| `brand_white` | `#F5F5F5` | Texto primário, ícones |
| `brand_yellow` | `#F5C518` | CTAs, destaques, badges, progresso |
| `brand_red` | `#D32F2F` | Alertas, ações destrutivas, acentos |

### Superfícies (Dark Theme)

| Token | Hex | Uso |
|---|---|---|
| `surface_background` | `#0A0A0A` | Fundo da tela |
| `surface_card` | `#1A1A1A` | Cards, containers |
| `surface_elevated` | `#242424` | Bottom sheets, modais, elementos elevados |
| `surface_overlay` | `#2E2E2E` | Hover states, elementos sobrepostos |

### Texto

| Token | Hex | Uso |
|---|---|---|
| `text_primary` | `#F5F5F5` | Texto principal |
| `text_secondary` | `#A0A0A0` | Texto secundário, labels, captions |
| `text_disabled` | `#5A5A5A` | Texto desabilitado |
| `text_on_yellow` | `#0A0A0A` | Texto sobre fundo amarelo |
| `text_on_red` | `#F5F5F5` | Texto sobre fundo vermelho |

### Estados

| Token | Hex | Uso |
|---|---|---|
| `state_success` | `#4CAF50` | Sucesso, confirmações |
| `state_warning` | `#F5C518` | Avisos (usa brand_yellow) |
| `state_error` | `#D32F2F` | Erros (usa brand_red) |
| `state_info` | `#42A5F5` | Informações neutras |

### Gradientes

| Token | Cores | Uso |
|---|---|---|
| `gradient_hero` | `#F5C518 → #D32F2F` | Banners hero, headers especiais |
| `gradient_overlay` | `#0A0A0A/0% → #0A0A0A/90%` | Overlay sobre thumbnails/imagens |
| `gradient_glass` | `#FFFFFF/5% → #FFFFFF/10%` | Efeito glass em superfícies elevadas |

---

## 3. Tipografia

| Estilo | Tamanho | Weight | Uso |
|---|---|---|---|
| `Display` | 32sp | Bold | Títulos de seção, hero text |
| `Headline` | 24sp | SemiBold | Títulos de tela |
| `Title` | 20sp | SemiBold | Títulos de card, nome de arco |
| `Body` | 16sp | Regular | Texto corrido, descrições |
| `Label` | 14sp | Medium | Labels, botões, chips |
| `Caption` | 12sp | Regular | Metadados, timestamps, episódio nº |

**Font family:** System default (Roboto no Android). Manter consistência com o ecossistema nativo.

---

## 4. Espaçamento

Sistema de espaçamento em múltiplos de 4dp:

| Token | Valor | Uso |
|---|---|---|
| `spacing_xs` | 4dp | Espaço mínimo entre ícone e texto |
| `spacing_sm` | 8dp | Espaço entre elementos internos de um card |
| `spacing_md` | 16dp | Padding padrão de containers |
| `spacing_lg` | 24dp | Espaço entre seções |
| `spacing_xl` | 32dp | Margens externas, separação de blocos |
| `spacing_2xl` | 48dp | Espaço grande, separação de grupos |

---

## 5. Bordas e Cantos

| Token | Valor | Uso |
|---|---|---|
| `radius_sm` | 8dp | Chips, badges, botões pequenos |
| `radius_md` | 12dp | Cards, inputs |
| `radius_lg` | 16dp | Bottom sheets, modais |
| `radius_xl` | 24dp | Cards hero, imagens featured |
| `radius_full` | 50% | Avatares, FABs |

---

## 6. Elevação e Efeitos

### Glassmorphism / Liquid Glass

Aplicação controlada para criar profundidade sem comprometer legibilidade:

```
Propriedades do efeito Glass:
- Background: linear-gradient(135deg, rgba(255,255,255,0.05), rgba(255,255,255,0.10))
- Border: 1px solid rgba(255,255,255,0.08)
- Blur: 20dp (backdrop)
- Aplicar APENAS em: cards de destaque, bottom sheets, player controls overlay
- NÃO aplicar em: botões, chips, inputs, cards de lista
```

### Sombras

| Nível | Uso |
|---|---|
| Nenhuma | Elementos no nível de superfície |
| Sutil (`0 2dp 8dp #000/20%`) | Cards padrão |
| Média (`0 4dp 16dp #000/30%`) | Bottom sheets, modais |
| Forte (`0 8dp 32dp #000/40%`) | Player overlay, FAB |

---

## 7. Iconografia

- **Estilo:** Outlined, stroke 1.5dp (Material Symbols Outlined).
- **Tamanho padrão:** 24dp.
- **Cor padrão:** `text_primary` (#F5F5F5).
- **Cor ativa/selecionada:** `brand_yellow` (#F5C518).
- Ícones customizados (se necessários) seguem o mesmo grid de 24dp.

---

## 8. Componentes Definidos

> Esta seção é atualizada conforme novos componentes são criados no projeto.

### HeroBanner

**Arquivo:** `features/home/presentation/components/HeroBanner.kt`

**Descricao:** Banner hero full-width exibido no topo da Home. Mostra o backdrop da serie com gradiente escuro de baixo para cima para garantir legibilidade do texto. Segue o padrao Netflix/Crunchyroll (Jakob's Law).

**Parametros:**
- `title: String` -- Titulo da serie (displayLarge)
- `tagline: String` -- Tagline descritiva (bodyLarge, cor secondary)
- `backdropUrl: String?` -- URL do backdrop 16:9 (Coil AsyncImage)
- `voteAverage: Double` -- Nota media, exibida em badge com estrela
- `modifier: Modifier` -- Modifier padrao

**Regras:**
- Aspect ratio fixo 16:9 para o container
- Gradiente overlay: transparente no topo, 95% opaco na base (garante contraste WCAG AA)
- Rating badge usa cor primary (amarelo) sobre surface semi-transparente
- Texto do titulo limitado a 1 linha, tagline a 2 linhas

---

### SeasonCard

**Arquivo:** `features/home/presentation/components/SeasonCard.kt`

**Descricao:** Card compacto representando um arco/temporada. Poster 2:3 com nome e contagem de episodios. Estado selecionado com borda amarela (primary).

**Parametros:**
- `name: String` -- Nome do arco
- `episodeCount: Int` -- Quantidade de episodios
- `posterUrl: String?` -- URL do poster 2:3
- `isSelected: Boolean` -- Estado de selecao (borda amarela + background elevado)
- `onClick: () -> Unit` -- Callback de clique
- `modifier: Modifier` -- Modifier padrao

**Regras:**
- Largura fixa 120dp (permite 2.5-3 cards visiveis, sinalizando scroll horizontal)
- Poster aspect ratio 2:3 (proporcao padrao de poster anime/TV)
- Selecionado: borda 2dp primary + background surfaceVariant + texto primary
- Nao selecionado: sem borda + background surface + texto onSurface
- Nome limitado a 2 linhas

---

### SeasonRow

**Arquivo:** `features/home/presentation/components/SeasonRow.kt`

**Descricao:** Secao horizontal com titulo "Arcos" e LazyRow de SeasonCards. Gerencia selecao visual do arco ativo.

**Parametros:**
- `seasons: List<Season>` -- Lista de arcos para exibir
- `selectedSeason: Season?` -- Arco atualmente selecionado
- `onSeasonClick: (Season) -> Unit` -- Callback ao clicar em um arco
- `modifier: Modifier` -- Modifier padrao

**Regras:**
- Titulo "Arcos" em headlineLarge com padding horizontal 16dp
- LazyRow com contentPadding horizontal 16dp e spacing 12dp entre cards
- Items usam key = season.id para otimizar recomposicao
- 12dp de espacamento vertical entre titulo e row

---

### EpisodeCard

**Arquivo:** `features/home/presentation/components/EpisodeCard.kt`

**Descricao:** Card horizontal para episodio em lista vertical. Thumbnail a esquerda (16:9), info a direita. Badge de numero do episodio em amarelo sobre o thumbnail.

**Parametros:**
- `episodeNumber: Int` -- Numero do episodio (exibido em badge "EP X")
- `name: String` -- Nome do episodio
- `stillUrl: String?` -- URL da imagem still do episodio
- `runtime: Int?` -- Duracao em minutos (exibido como "X min", oculto se null)
- `onClick: () -> Unit` -- Callback de clique
- `modifier: Modifier` -- Modifier padrao

**Regras:**
- Altura fixa 100dp, largura full-width
- Thumbnail com aspect ratio 16:9 (corresponde ao conteudo de video)
- Badge "EP X" posicionado no canto inferior-esquerdo do thumbnail, cor primary/onPrimary
- Nome limitado a 2 linhas
- Card inteiro e touch target (excede 48x48dp minimo WCAG 2.5.5)

---

<!-- Template para novos componentes:
### ComponentName

**Arquivo:** `core/ui/components/ComponentName.kt`

**Descrição:** Breve descrição do componente.

**Variantes:**
- Variante 1: descrição
- Variante 2: descrição

**Props/Parâmetros:**
- `param1: Type` — descrição
- `param2: Type` — descrição

**Uso:**
```kotlin
ComponentName(
    param1 = value,
    param2 = value
)
```

**Regras:**
- Regra de uso 1
- Regra de uso 2
-->

---

## 9. Padrões de Tela

> Esta seção é atualizada conforme novos padrões de tela são definidos.

### Estrutura Base de Tela

Toda tela segue a estrutura:

```
┌─────────────────────────┐
│      Top App Bar        │  ← Título + ações contextuais
├─────────────────────────┤
│                         │
│                         │
│      Content Area       │  ← Scroll vertical, padding horizontal 16dp
│                         │
│                         │
├─────────────────────────┤
│    Bottom Nav Bar       │  ← Navegação principal (se aplicável)
└─────────────────────────┘
```

### Home Screen

**Arquivo:** `features/home/presentation/HomeScreen.kt`

**Padrao:** LazyColumn vertical com tres secoes distintas, inspirado em apps de streaming (Netflix, Crunchyroll, Disney+).

```
+---------------------------+
|       HeroBanner          |  -- Backdrop 16:9 + gradiente + titulo + rating
|       (full-width)        |
+---------------------------+
|       24dp spacing        |
+---------------------------+
| Arcos (headlineLarge)     |
| [Card][Card][Card]>>>     |  -- SeasonRow (LazyRow horizontal)
+---------------------------+
|       24dp spacing        |
+---------------------------+
| Episodios (headlineLarge) |
| [EpisodeCard]             |  -- Lista vertical de EpisodeCards
| [EpisodeCard]             |     padding horizontal 16dp, vertical 6dp
| [EpisodeCard]             |
+---------------------------+
```

**Separacao Screen/Content:**
- `HomeScreen` (stateful): injeta ViewModel via `hiltViewModel()`, coleta `uiState` com `collectAsStateWithLifecycle()`
- `HomeContent` (stateless): funcao pura dos parametros, previewable e testavel

**Estados tratados:**
- Loading: CircularProgressIndicator centralizado (carga inicial)
- Error: Mensagem + botao "Tentar novamente" (CTA amarelo)
- Success: Layout completo com hero + arcos + episodios
- Loading episodes: CircularProgressIndicator abaixo da SeasonRow

---

### Estados de Tela

Toda tela deve tratar 4 estados visuais:

| Estado | Comportamento |
|---|---|
| **Loading** | Skeleton/shimmer nos containers de conteúdo |
| **Success** | Conteúdo renderizado normalmente |
| **Error** | Mensagem de erro + botão "Tentar novamente" |
| **Empty** | Ilustração + mensagem descritiva |

---

## 10. Animações e Transições

| Tipo | Duração | Curva | Uso |
|---|---|---|---|
| Navegação entre telas | 300ms | EaseInOut | Transições de tela |
| Fade in de conteúdo | 200ms | EaseOut | Aparecimento de elementos |
| Hover/Press | 100ms | EaseIn | Feedback de interação |
| Shimmer loading | 1500ms | Linear (loop) | Skeleton screens |

**Regras:**
- Animações devem ser sutis e funcionais — nunca decorativas.
- Respeitar preferências de acessibilidade (`reduceMotion`).
- Player transitions devem ser fluidas (sem jank).
