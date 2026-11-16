package com.ninno.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import com.ninno.app.resources.Res
import com.ninno.app.resources.noto_sans_arabic
import org.jetbrains.compose.resources.Font

/** Langues dans lesquelles Zoé peut parler. [code] : ISO 639-1, persisté en localStorage. */
enum class Language(val code: String, val nativeName: String) {
    FR("fr", "Français"),
    EN("en", "English"),
    ES("es", "Español"),
    PT("pt", "Português"),
    DE("de", "Deutsch"),
    AR("ar", "العربية"),
    RU("ru", "Русский");

    companion object {
        fun fromCode(code: String?): Language = entries.firstOrNull { it.code == code } ?: FR
    }
}

/** Erreurs de génération de formule, traduites à l'affichage. */
enum class FormulaError { INCOMPLETE, NOTHING_LIKED, GENERATION_FAILED }

/** Tous les textes du parcours enfant (Zoé, règles, tri, formule). */
class ZoeStrings(
    val homeTitle: String,
    val homeWith: String,
    val zoeName: String,
    val greeting: (childName: String?) -> String,
    val welcome: String,
    val readyToStart: String,
    val howToIntro: String,
    val secondary: List<String>,
    val third: List<String>,
    val fourth: List<String>,
    val fifth: List<String>,
    val seventh: List<String>,
    val topNote: String,
    val heartNote: String,
    val baseNote: String,
    val like: String,
    val dislike: String,
    val rulesTitle: String,
    val rules: List<Pair<String, String>>,
    val bravo: String,
    val preparing: String,
    val hereIsYourPerfume: String,
    val oops: String,
    val yourFormula: String,
    val forBottle: (ml: Int) -> String,
    val topNotes: String,
    val heartNotes: String,
    val baseNotes: String,
    val formulaFailed: String,
    val error: (FormulaError) -> String,
)

val LocalLanguage = compositionLocalOf { Language.FR }
val LocalStrings = compositionLocalOf { zoeStrings(Language.FR, null) }

/** Textes pour une langue, accordés au genre de l'enfant quand la langue l'exige. */
fun zoeStrings(language: Language, gender: ChildGender?): ZoeStrings = when (language) {
    Language.FR -> french(gender)
    Language.EN -> english()
    Language.ES -> spanish(gender)
    Language.PT -> portuguese(gender)
    Language.DE -> german()
    Language.AR -> arabic(gender)
    Language.RU -> russian(gender)
}

/** Accord générique : forme inclusive « m/f » quand le genre est inconnu. */
private fun ChildGender?.pick(m: String, f: String, unknown: String = "$m/$f") = when (this) {
    ChildGender.BOY -> m
    ChildGender.GIRL -> f
    null -> unknown
}

private fun french(g: ChildGender?) = ZoeStrings(
    homeTitle = "Crée ton parfum",
    homeWith = "avec",
    zoeName = "Zoé la taupe",
    greeting = { if (it.isNullOrBlank()) "Coucou ! Moi, c'est Zoé la taupe." else "Coucou $it ! Moi, c'est Zoé la taupe." },
    welcome = "Bienvenue au Studio des Parfums !",
    readyToStart = "Es-tu ${g.agree("prêt", "prête")} à commencer ?",
    howToIntro = "Avant de commencer, voici quelques infos !",
    secondary = listOf("Un parfum est un mélange d'odeurs, d'alcool et un peu d'eau."),
    third = listOf("Un parfum est composé de :", "la note de tête,", "la note de cœur,", "et la note de fond."),
    fourth = listOf(
        "On va commencer par la note de tête : c'est la plus légère.",
        "Sens, et choisis la meilleure puis celle que tu n'aimes pas.",
        "C'est la ligne du haut."
    ),
    fifth = listOf(
        "Ensuite, la note de cœur : le caractère.",
        "Sens, et choisis les 2 meilleures puis celle que tu n'aimes pas.",
        "C'est la ligne du milieu."
    ),
    seventh = listOf(
        "Enfin, la note de fond : la puissance.",
        "Sens, et choisis les 2 meilleures puis celle que tu n'aimes pas.",
        "C'est la ligne du bas."
    ),
    topNote = "Note de tête",
    heartNote = "Note de cœur",
    baseNote = "Note de fond",
    like = "J'aime",
    dislike = "Je n'aime pas",
    rulesTitle = "Comment ça marche ?",
    rules = listOf(
        "Un clic" to "sur une note affiche son nom.",
        "Un double clic" to "affiche sa description : ce qu'elle sent et ce qu'elle évoque.",
        "Glisse la note" to "dans « J'aime » ou « Je n'aime pas » pour faire ton choix. Pour changer d'avis, sors-la de sa case.",
        "Deux notes par case" to "à chaque étape : tes deux préférées et les deux que tu aimes le moins.",
    ),
    bravo = "Bravo !",
    preparing = "Je prépare ton parfum...",
    hereIsYourPerfume = "Voici ton parfum :",
    oops = "Oups !",
    yourFormula = "Ta formule",
    forBottle = { "Pour un flacon de $it mL" },
    topNotes = "Notes de tête",
    heartNotes = "Notes de cœur",
    baseNotes = "Notes de fond",
    formulaFailed = "Ta formule n'a pas pu être créée.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Réponses incomplètes"
            FormulaError.NOTHING_LIKED -> "Tu n'as aimé aucune note : recommence pour créer ton parfum !"
            FormulaError.GENERATION_FAILED -> "Impossible de générer ta formule pour le moment."
        }
    },
)

private fun english() = ZoeStrings(
    homeTitle = "Create your perfume",
    homeWith = "with",
    zoeName = "Zoé the Mole",
    greeting = { if (it.isNullOrBlank()) "Hi there! I'm Zoé the Mole." else "Hi $it! I'm Zoé the Mole." },
    welcome = "Welcome to the Perfume Studio!",
    readyToStart = "Are you ready to start?",
    howToIntro = "Before we start, here are a few tips!",
    secondary = listOf("A perfume is a mix of scents, alcohol and a little water."),
    third = listOf("A perfume is made of:", "the top note,", "the heart note,", "and the base note."),
    fourth = listOf(
        "Let's start with the top note: it's the lightest one.",
        "Smell them, then pick your favourite and the one you don't like.",
        "It's the top row."
    ),
    fifth = listOf(
        "Next, the heart note: the character.",
        "Smell them, then pick your 2 favourites and the one you don't like.",
        "It's the middle row."
    ),
    seventh = listOf(
        "Finally, the base note: the strength.",
        "Smell them, then pick your 2 favourites and the one you don't like.",
        "It's the bottom row."
    ),
    topNote = "Top note",
    heartNote = "Heart note",
    baseNote = "Base note",
    like = "I like",
    dislike = "I don't like",
    rulesTitle = "How does it work?",
    rules = listOf(
        "One click" to "on a note shows its name.",
        "A double click" to "shows its description: what it smells like and what it reminds you of.",
        "Drag the note" to "into “I like” or “I don't like” to choose. Changed your mind? Drag it out of its box.",
        "Two notes per box" to "at each step: your two favourites and the two you like least.",
    ),
    bravo = "Well done!",
    preparing = "I'm making your perfume...",
    hereIsYourPerfume = "Here is your perfume:",
    oops = "Oops!",
    yourFormula = "Your formula",
    forBottle = { "For a $it mL bottle" },
    topNotes = "Top notes",
    heartNotes = "Heart notes",
    baseNotes = "Base notes",
    formulaFailed = "Your formula couldn't be created.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Some answers are missing"
            FormulaError.NOTHING_LIKED -> "You didn't like any note: try again to create your perfume!"
            FormulaError.GENERATION_FAILED -> "Your formula can't be created right now."
        }
    },
)

private fun spanish(g: ChildGender?) = ZoeStrings(
    homeTitle = "Crea tu perfume",
    homeWith = "con",
    zoeName = "Zoé la topita",
    greeting = { if (it.isNullOrBlank()) "¡Hola! Soy Zoé la topita." else "¡Hola, $it! Soy Zoé la topita." },
    welcome = "¡${g.pick("Bienvenido", "Bienvenida", "Te damos la bienvenida")} al Estudio de Perfumes!",
    readyToStart = "¿Estás ${g.pick("listo", "lista")} para empezar?",
    howToIntro = "Antes de empezar, ¡aquí tienes algunos consejos!",
    secondary = listOf("Un perfume es una mezcla de olores, alcohol y un poco de agua."),
    third = listOf("Un perfume se compone de:", "la nota de salida,", "la nota de corazón,", "y la nota de fondo."),
    fourth = listOf(
        "Empezamos por la nota de salida: es la más ligera.",
        "Huele y elige tu favorita y luego la que no te gusta.",
        "Es la fila de arriba."
    ),
    fifth = listOf(
        "Después, la nota de corazón: el carácter.",
        "Huele y elige tus 2 favoritas y luego la que no te gusta.",
        "Es la fila del medio."
    ),
    seventh = listOf(
        "Por último, la nota de fondo: la fuerza.",
        "Huele y elige tus 2 favoritas y luego la que no te gusta.",
        "Es la fila de abajo."
    ),
    topNote = "Nota de salida",
    heartNote = "Nota de corazón",
    baseNote = "Nota de fondo",
    like = "Me gusta",
    dislike = "No me gusta",
    rulesTitle = "¿Cómo funciona?",
    rules = listOf(
        "Un clic" to "en una nota muestra su nombre.",
        "Un doble clic" to "muestra su descripción: a qué huele y qué te recuerda.",
        "Arrastra la nota" to "a «Me gusta» o «No me gusta» para elegir. Si cambias de opinión, sácala de su casilla.",
        "Dos notas por casilla" to "en cada etapa: tus dos favoritas y las dos que menos te gustan.",
    ),
    bravo = "¡Bravo!",
    preparing = "Estoy preparando tu perfume...",
    hereIsYourPerfume = "Aquí está tu perfume:",
    oops = "¡Uy!",
    yourFormula = "Tu fórmula",
    forBottle = { "Para un frasco de $it mL" },
    topNotes = "Notas de salida",
    heartNotes = "Notas de corazón",
    baseNotes = "Notas de fondo",
    formulaFailed = "No se ha podido crear tu fórmula.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Respuestas incompletas"
            FormulaError.NOTHING_LIKED -> "No te ha gustado ninguna nota: ¡vuelve a empezar para crear tu perfume!"
            FormulaError.GENERATION_FAILED -> "No se puede crear tu fórmula en este momento."
        }
    },
)

private fun portuguese(g: ChildGender?) = ZoeStrings(
    homeTitle = "Cria o teu perfume",
    homeWith = "com",
    zoeName = "Zoé, a toupeira",
    greeting = { if (it.isNullOrBlank()) "Olá! Eu sou a Zoé, a toupeira." else "Olá, $it! Eu sou a Zoé, a toupeira." },
    welcome = "${g.pick("Bem-vindo", "Bem-vinda", "Boas-vindas")} ao Estúdio dos Perfumes!",
    readyToStart = "Estás ${g.pick("pronto", "pronta")} para começar?",
    howToIntro = "Antes de começar, aqui ficam algumas dicas!",
    secondary = listOf("Um perfume é uma mistura de cheiros, álcool e um pouco de água."),
    third = listOf("Um perfume é composto por:", "a nota de topo,", "a nota de coração,", "e a nota de fundo."),
    fourth = listOf(
        "Vamos começar pela nota de topo: é a mais leve.",
        "Cheira e escolhe a tua preferida e depois a que não gostas.",
        "É a linha de cima."
    ),
    fifth = listOf(
        "Depois, a nota de coração: o carácter.",
        "Cheira e escolhe as tuas 2 preferidas e depois a que não gostas.",
        "É a linha do meio."
    ),
    seventh = listOf(
        "Por fim, a nota de fundo: a força.",
        "Cheira e escolhe as tuas 2 preferidas e depois a que não gostas.",
        "É a linha de baixo."
    ),
    topNote = "Nota de topo",
    heartNote = "Nota de coração",
    baseNote = "Nota de fundo",
    like = "Gosto",
    dislike = "Não gosto",
    rulesTitle = "Como funciona?",
    rules = listOf(
        "Um clique" to "numa nota mostra o seu nome.",
        "Um duplo clique" to "mostra a sua descrição: o cheiro e o que te faz lembrar.",
        "Arrasta a nota" to "para «Gosto» ou «Não gosto» para escolher. Se mudares de ideias, tira-a da caixa.",
        "Duas notas por caixa" to "em cada etapa: as tuas duas preferidas e as duas de que gostas menos.",
    ),
    bravo = "Parabéns!",
    preparing = "Estou a preparar o teu perfume...",
    hereIsYourPerfume = "Aqui está o teu perfume:",
    oops = "Ups!",
    yourFormula = "A tua fórmula",
    forBottle = { "Para um frasco de $it mL" },
    topNotes = "Notas de topo",
    heartNotes = "Notas de coração",
    baseNotes = "Notas de fundo",
    formulaFailed = "Não foi possível criar a tua fórmula.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Respostas incompletas"
            FormulaError.NOTHING_LIKED -> "Não gostaste de nenhuma nota: recomeça para criar o teu perfume!"
            FormulaError.GENERATION_FAILED -> "De momento, não é possível criar a tua fórmula."
        }
    },
)

private fun german() = ZoeStrings(
    homeTitle = "Kreiere dein Parfum",
    homeWith = "mit",
    zoeName = "Zoé, dem Maulwurf",
    greeting = { if (it.isNullOrBlank()) "Hallo! Ich bin Zoé, der Maulwurf." else "Hallo $it! Ich bin Zoé, der Maulwurf." },
    welcome = "Willkommen im Parfum-Studio!",
    readyToStart = "Bist du bereit?",
    howToIntro = "Bevor wir anfangen, hier ein paar Tipps!",
    secondary = listOf("Ein Parfum ist eine Mischung aus Düften, Alkohol und ein bisschen Wasser."),
    third = listOf("Ein Parfum besteht aus:", "der Kopfnote,", "der Herznote", "und der Basisnote."),
    fourth = listOf(
        "Wir beginnen mit der Kopfnote: Sie ist die leichteste.",
        "Riech daran und wähle deine liebste und dann die, die du nicht magst.",
        "Das ist die obere Reihe."
    ),
    fifth = listOf(
        "Danach die Herznote: der Charakter.",
        "Riech daran und wähle deine 2 liebsten und dann die, die du nicht magst.",
        "Das ist die mittlere Reihe."
    ),
    seventh = listOf(
        "Zum Schluss die Basisnote: die Kraft.",
        "Riech daran und wähle deine 2 liebsten und dann die, die du nicht magst.",
        "Das ist die untere Reihe."
    ),
    topNote = "Kopfnote",
    heartNote = "Herznote",
    baseNote = "Basisnote",
    like = "Mag ich",
    dislike = "Mag ich nicht",
    rulesTitle = "Wie funktioniert das?",
    rules = listOf(
        "Ein Klick" to "auf eine Note zeigt ihren Namen.",
        "Ein Doppelklick" to "zeigt ihre Beschreibung: wonach sie riecht und woran sie dich erinnert.",
        "Zieh die Note" to "in „Mag ich“ oder „Mag ich nicht“, um zu wählen. Meinung geändert? Zieh sie wieder heraus.",
        "Zwei Noten pro Feld" to "bei jedem Schritt: deine zwei liebsten und die zwei, die du am wenigsten magst.",
    ),
    bravo = "Super!",
    preparing = "Ich mache dein Parfum...",
    hereIsYourPerfume = "Hier ist dein Parfum:",
    oops = "Hoppla!",
    yourFormula = "Deine Formel",
    forBottle = { "Für eine $it-mL-Flasche" },
    topNotes = "Kopfnoten",
    heartNotes = "Herznoten",
    baseNotes = "Basisnoten",
    formulaFailed = "Deine Formel konnte nicht erstellt werden.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Unvollständige Antworten"
            FormulaError.NOTHING_LIKED -> "Dir hat keine Note gefallen: Fang noch mal an, um dein Parfum zu kreieren!"
            FormulaError.GENERATION_FAILED -> "Deine Formel kann gerade nicht erstellt werden."
        }
    },
)

/** Arabe : l'impératif et la 2e personne s'accordent ; genre inconnu → masculin générique. */
private fun arabic(gender: ChildGender?): ZoeStrings {
    fun g(m: String, f: String) = if (gender == ChildGender.GIRL) f else m
    return ZoeStrings(
        homeTitle = g("اصنع عطرك", "اصنعي عطرك"),
        homeWith = "مع",
        zoeName = "زوي الخُلد",
        greeting = { if (it.isNullOrBlank()) "مرحبًا! أنا زوي الخُلد." else "مرحبًا يا $it! أنا زوي الخُلد." },
        welcome = "أهلًا بك في استوديو العطور!",
        readyToStart = g("هل أنت مستعد للبدء؟", "هل أنتِ مستعدة للبدء؟"),
        howToIntro = "قبل أن نبدأ، إليك بعض المعلومات!",
        secondary = listOf("العطر هو مزيج من الروائح والكحول وقليل من الماء."),
        third = listOf("يتكوّن العطر من:", "النوتة العليا،", "نوتة القلب،", "والنوتة الأساسية."),
        fourth = listOf(
            "سنبدأ بالنوتة العليا: إنها الأخفّ.",
            g("شُمّ واختر الأفضل، ثم التي لا تحبها.", "شُمّي واختاري الأفضل، ثم التي لا تحبينها."),
            "إنه الصف العلوي."
        ),
        fifth = listOf(
            "بعد ذلك، نوتة القلب: الشخصية.",
            g("شُمّ واختر أفضل نوتتين، ثم التي لا تحبها.", "شُمّي واختاري أفضل نوتتين، ثم التي لا تحبينها."),
            "إنه الصف الأوسط."
        ),
        seventh = listOf(
            "وأخيرًا، النوتة الأساسية: القوة.",
            g("شُمّ واختر أفضل نوتتين، ثم التي لا تحبها.", "شُمّي واختاري أفضل نوتتين، ثم التي لا تحبينها."),
            "إنه الصف السفلي."
        ),
        topNote = "النوتة العليا",
        heartNote = "نوتة القلب",
        baseNote = "النوتة الأساسية",
        like = "أحب",
        dislike = "لا أحب",
        rulesTitle = "كيف تعمل اللعبة؟",
        rules = listOf(
            "نقرة واحدة" to "على النوتة تُظهر اسمها.",
            "نقرة مزدوجة" to "تُظهر وصفها: رائحتها وما تذكّرك به.",
            g("اسحب النوتة", "اسحبي النوتة") to g(
                "إلى «أحب» أو «لا أحب» لتختار. إذا غيّرت رأيك، أخرجها من خانتها.",
                "إلى «أحب» أو «لا أحب» لتختاري. إذا غيّرتِ رأيك، أخرجيها من خانتها."
            ),
            "نوتتان في كل خانة" to g(
                "في كل مرحلة: النوتتان المفضّلتان لديك، والنوتتان اللتان تحبهما أقل.",
                "في كل مرحلة: النوتتان المفضّلتان لديكِ، والنوتتان اللتان تحبينهما أقل."
            ),
        ),
        bravo = g("أحسنت!", "أحسنتِ!"),
        preparing = "أنا أُحضّر عطرك...",
        hereIsYourPerfume = "هذا عطرك:",
        oops = "عفوًا!",
        yourFormula = "تركيبتك",
        forBottle = { "لقارورة سعتها $it مل" },
        topNotes = "النوتات العليا",
        heartNotes = "نوتات القلب",
        baseNotes = "النوتات الأساسية",
        formulaFailed = "تعذّر إنشاء تركيبتك.",
        error = {
            when (it) {
                FormulaError.INCOMPLETE -> "الإجابات غير مكتملة"
                FormulaError.NOTHING_LIKED -> g(
                    "لم تُعجبك أي نوتة: أعد المحاولة لصنع عطرك!",
                    "لم تُعجبكِ أي نوتة: أعيدي المحاولة لصنع عطرك!"
                )
                FormulaError.GENERATION_FAILED -> "لا يمكن إنشاء تركيبتك حاليًا."
            }
        },
    )
}

private fun russian(g: ChildGender?) = ZoeStrings(
    homeTitle = "Создай свой парфюм",
    homeWith = "вместе с",
    zoeName = "кротихой Зоэ",
    greeting = { if (it.isNullOrBlank()) "Привет! Я кротиха Зоэ." else "Привет, $it! Я кротиха Зоэ." },
    welcome = "Добро пожаловать в Парфюмерную студию!",
    readyToStart = "Ты ${g.pick("готов", "готова", "готов(а)")} начать?",
    howToIntro = "Перед началом — немного информации!",
    secondary = listOf("Парфюм — это смесь ароматов, спирта и немного воды."),
    third = listOf("Парфюм состоит из:", "верхней ноты,", "ноты сердца", "и базовой ноты."),
    fourth = listOf(
        "Начнём с верхней ноты: она самая лёгкая.",
        "Понюхай и выбери самую любимую, а потом ту, что не нравится.",
        "Это верхний ряд."
    ),
    fifth = listOf(
        "Дальше — нота сердца: это характер.",
        "Понюхай и выбери 2 самые любимые, а потом ту, что не нравится.",
        "Это средний ряд."
    ),
    seventh = listOf(
        "И наконец, базовая нота: это сила.",
        "Понюхай и выбери 2 самые любимые, а потом ту, что не нравится.",
        "Это нижний ряд."
    ),
    topNote = "Верхняя нота",
    heartNote = "Нота сердца",
    baseNote = "Базовая нота",
    like = "Нравится",
    dislike = "Не нравится",
    rulesTitle = "Как это работает?",
    rules = listOf(
        "Один клик" to "по ноте показывает её название.",
        "Двойной клик" to "показывает её описание: как она пахнет и что напоминает.",
        "Перетащи ноту" to "в «Нравится» или «Не нравится», чтобы выбрать. Передумал(а)? Вытащи её из ячейки."
            .let { if (g == null) it else it.replace("Передумал(а)", g.pick("Передумал", "Передумала")) },
        "Две ноты в ячейке" to "на каждом этапе: две самые любимые и две, что нравятся меньше всего.",
    ),
    bravo = "Молодец!",
    preparing = "Я готовлю твой парфюм...",
    hereIsYourPerfume = "Вот твой парфюм:",
    oops = "Ой!",
    yourFormula = "Твоя формула",
    forBottle = { "Для флакона $it мл" },
    topNotes = "Верхние ноты",
    heartNotes = "Ноты сердца",
    baseNotes = "Базовые ноты",
    formulaFailed = "Не удалось создать твою формулу.",
    error = {
        when (it) {
            FormulaError.INCOMPLETE -> "Ответы неполные"
            FormulaError.NOTHING_LIKED -> "Тебе не понравилась ни одна нота: начни заново, чтобы создать свой парфюм!"
            FormulaError.GENERATION_FAILED -> "Сейчас не получается создать твою формулу."
        }
    },
)

/**
 * Le rendu Skia du web n'a pas de police système : on charge Noto Sans Arabic comme police de repli
 * (sinon les glyphes arabes s'affichent en carrés).
 */
@Composable
fun ArabicFontFallback() {
    // Sur le web, Font() renvoie d'abord une police vide puis la vraie une fois chargée : on préchauffe à chaque changement
    val font = Font(Res.font.noto_sans_arabic)
    val resolver = LocalFontFamilyResolver.current
    LaunchedEffect(font) { resolver.preload(FontFamily(font)) }
}
