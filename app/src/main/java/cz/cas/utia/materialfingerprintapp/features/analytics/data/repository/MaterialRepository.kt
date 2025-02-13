package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import android.graphics.Bitmap
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

interface MaterialRepository {
    suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary>

    suspend fun getAllSimilarMaterialsOrderedByName(materialId: Long): List<MaterialSummary>
    suspend fun getAllSimilarMaterialsOrderedByName(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<MaterialSummary>

    suspend fun getSimilarMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String, materialId: Long): List<MaterialSummary>
    suspend fun getSimilarMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String, materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    //todo tato metoda bude vubec potreba? nebo jen pridat characteristics do getMaterialsOrderedByName(..)?
    suspend fun getAllMaterialsSortedBySimilarity(characteristics: MaterialCharacteristics): List<MaterialSummary> //room material repository bude mit private servicku, ktera mu to spocita

    suspend fun getMaterialsCount(): Long
    suspend fun getMaterial(id: Long): Material //todo nebo to lze dat niz jen k tem repum, ktere to potrebuji?
    suspend fun getPolarPlot(id: Long): Bitmap //todo later change to svg

    //todo obrazovka ApplyFilterOnPolarPlot(characteristics: MaterialCharacteristics)

    //todo obrazovka PolarPlotVisualization:
    // polarPlotVisalization(polarPlot1, materialCharacteristics) ALE VLASTNE NELZE PREDAVAT TAKHLE SLOZITE OBJEKTY V NAVIGACI ASI.. :(
    // polarPlotVisalization(polarPlot1, polarPlot2, polarPlotWithBothMaterials) - viz gogole docs s popisem
    // takze polarPlotVisalualisation(mat1: dvojice id a local/remote, mat2: to same, ale nullable) = udelat si na to novy objekt
}

// todo obrazovka apply filter: pres Proto Data Store ukladat ten objekt 16 statistik a nacitat si ho pak i v BrowseMaterialsVM, takze do composables predavat krome ID i treba ID = -1 jakoze to nema ID a maji se nacist statistiky z data store

//todo 1:
// na BrowseMaterials screenach udelat zavislost na obou viewmodelech, protoze tam pridam i dialog, kde se uzivatel rozhodne, zda najit similar material k lokalnim nebo remote datum

//todo 2:
// az budu delat BrowseSimilarMaterials obrazovku, tak protoze ty similar materialy budou ulozeny lokalne v RAMce (jen ty jejich summaries), tak lze pouzit BrowseLocalMaterialsViewModel, protoze ten
// vyhledava rychle (bez toho debounce) mezi materialy, akorat je potreba mu dat novy repository (SimilarMaterialsRepository), ktery bude in memory (v RAMce proste) a bude
// tam mit ulozene vsechny ty podobne materialy (jejich summaries)
// Ale stejne jako BrowseLocalMaterialsViewModel bude potrebovat mit i odkaz na RemoteRepository, protoze bude potrebovat umet hledat polar ploty a similar materialy
// Takze bych si sam implementoval hledani v Listu dle categorie a name a kdyz by uzivatel chtel Hledat podobny material k tomuhle podobnemu, co mam v inmemory Listu, tak udelat zase dialog s vyberem local nebo remote a podle toho volat dany repository a zyvslednych dat zas vytvorit novy repository s novym Listem
// Ten treti repozitar u BrowseSimilarMaterials viewmodelu (ten in memory repository) bude mit jen tuto metodu: getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<Material> a zbytek metod se bude normalne volat pres lokalnim nebo remote repositar

//todo 3:
// takze ten viewmodel pro similar materials bude muset vedet, zda amterialy, co zobrazuje, jsou lokalni nebo remote - protoze nebudez nat jejich 16 statistik a bude si je muset zjistit, aby je mohl predat dal - a aby vedel jestli u create polar plot ma tahat graf z lokalni db nebo z te remote = u tech materialu musi byt server ID nebo local ID, aspon jedno musi nebyt null - to zajistit
// A neudelat si cache na serveru i lokalne?
// A view model pro similarMaterials screenu bude muset mit nejaky init blok, ve kterem se zavola ten in memory repository (pretypovat si ho tam na ten InMemory, aby ta metoda sla volat) a rekne se mu, at si nacte data a preda se mu, jestli LOKAL nebo REMOTE a IDcko (to bude brat ta browse similar materials screen v parametru navigace)

//todo takze kazdy u find similar materials se pouzije podtrida local view modelu, protoze ten local view model bude brat ten main repository (to bude local) a pak jako private val si vezme i remote repository, kdezto ted bude potreba i ten inMemory(Similar)Repository
// anebo tomu browse local amterials viewmodelu dat nullable parametr pro ten treti repository (coz by byl ten LocalRepository protoze pro pripad, ze by se tenhle viewmodel instancioval pro ten SimilarScreen, tak by mel ten similar repository jako hlavni)  a pridat tam if je to null, tak to nepouzij, jinak to pouzij - ale to je takovy divny, takze to spis udelat pres subtype toho localniho
// A az tohle bude, tak pokud jsem to udelal takto, jak tu pisu, tak to popsat do DP kdyz budu chtit - a zminit, ze bylo nutne to rozdelit, protoze kdybych pouzival jen ty dva puvodni view modely a volal pri kazdem filtrovani materialu i pripadne hledani podobnych, tak by se to nemuselo implementacne odlisovat ale pokud by v databazi byly tisice materialu, tak by to bylo velice neefektivni pro server (ale i pro lokalni stroj, i kdyz tam nepredpokladam ze by toho uzivatel nafotil tisice)

//udelat to teda tak, ze ta data budou jen ve state a ze se pdoobny material pokazdy najde jak lokalne, tak vzdalene a ze se pripadne v budoucnu muze udelat cache na server (stejna i lokalne), ktera to pak urychli, aby se pri kazdem filtru znova nehledal podobny material

//todo 4:
// a servicku pro pocitání podobného materiálu udělat tak, ze repositrář ji bude volat, nikoli přímo ti viewmodelové, co ji budou potřebovat, protože oni sami vědí, jaký repositář jim to má udělat, takže oni sami si ho zavolají a až ta implementace lokálního room repositáře bude mít u sebe tu srevicku, aby si mohla nechat ty materiály porovnat dle té podobnosti

//todo DULEZITE - predelat to zpatky na Material namisto MaterialSummary protoze budu polar ploty kreslit sam a zadny SVG nebudou potreba -> nebude getPolarPlot() ani v local ani v remote - composable na kresleni polar plotu dostane ID materialu a informaci jestli je lokalni nebo remote a jeji viewmodel si sam fetchne 16 charakteristik (nebo 32 pro dva grafy) a composable je vykresli
// ny vykresleni tech polar plotu z remote: Protoze uz mam ve state v browse materials screen ulozene ty 16tice charakteristik, tak nechci je znovu tahat ze serveru -> ulozit je do data store pod nejaky klic "remoteMaterialCharacteristics1" a "...2" a v navigaci rict jen REMOTE/LOCAL, Long idcko, Long? idcko (jakoze nullable protoze klidne muze byt jen jedno) -> a v viewmodelu polar plot screen si jen precist jestli je i to druhe null a kdyz je, tak vykreslit jen to z prvniho slotu, jinak oba
