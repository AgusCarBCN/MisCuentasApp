[33m9b5973c[m[33m ([m[1;36mHEAD -> [m[1;32mfeature/notifications[m[33m)[m configuro app para pedir permiso al usuario para recibir notificaciones
[33md7d744a[m corrijo pagina about
[33m06fb44d[m consigo solucionar el problema de las notificaciones
[33m421e9d3[m otra prueba
[33m21c74cc[m me quedo aqui hoy
[33mcc110e4[m y aqui acabo con las notificaciones y solo falta probar
[33m03f80fe[m hasta aqui me quedo hoy
[33m1089812[m notificaciones por debajo del balance
[33m191b3ac[m notificaciones para informes semanales y mensuales
[33m6e11984[m mejoras en fragments
[33m58c425b[m hasta aqui llego
[33m3f8e7ef[m funcionalidad de notificaciones de informe diario terminada
[33mce4e689[m intento patron de diseño observador
[33mc527516[m[33m ([m[1;32mmain[m[33m)[m añado barra de progreso a switch de alerta de saldo bajo
[33m0b92f12[m mejora en pagina de login
[33m4e109b4[m[33m ([m[1;32mfeature/v2[m[33m, [m[1;32mdevelop[m[33m)[m validacion de campos obligatorios en createuseractivity y saludo en funcion de la hora
[33m98921be[m preparado para commit
[33m361a82e[m preparados para comenzar en serio con las notificaciones
[33me1d5c03[m pruebas con notificaciones programadas ok
[33m22d0022[m consigo programar las notificaciones
[33m6dd4f49[m las notificaciones funcionan..ahora a programarlas
[33m77178d5[m consigo crear notificaciones simples
[33m114e77a[m aqui dejo de momento la correcion del formato de punto
[33ma2fdb27[m consigo resolver el formato de punto en la calculadora...genial!!!
[33mc6d6039[m mejoro apariencia de barra de seekbar
[33me64e09b[m acabo mejoras en switch
[33m77741fe[m añado de manera automatica el punto de separador de miles
[33m769ad8b[m personalizacion de todos los switch
[33m7996321[m seekbar personalizada
[33m9090e4a[m personalizacion de switch
[33mba39e62[m hasta aqui por hoy
[33m687d5f0[m creacion de layout de orientacion horizontal de notificaciones
[33m6ff4fa8[m creacion de layout de notificaciones
[33m64bd8a7[m creo las vistas para las notificaciones
[33m4d408fc[m preparado para commit
[33mfdf06ca[m el usuario puede elegir divisa base.Mejoro velocidad de carga de grafico con coroutines
[33ma0bf46e[m funcionalidad cambio de divisas con moneda base de euro
[33md04fb03[m consigo consumir datos de api de divisas con retrofit
[33md72d699[m hasta aqui he llegado
[33mbf5d014[m creacion de clases para ConversionResponse e interfaz CurrencyConversionApi
[33ma0f3161[m dependencias para usar retrofit y permisos para conectarse a internet en manifest
[33md99033e[m verifico cambio de divisas con toast
[33mfc872cc[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/develop[m[33m)[m preparado para merge
[33me43ac22[m funcionalidad importar datos terminada
[33m836c637[m funcionalidad de importar archivo csv en marcha
[33mdff5380[m por fin consigo que el usuario pueda escoger un archivo y que empiece a funcionar el import
[33m4428810[m consigo exportar a csv y que el usuario puedo ubicarlo en usuario y nombrar ese archivo
[33m0ed371e[m implementada la funcion exportar a archivo csv
[33m2dc7551[m de momento me quedo aqui .puedo escribir archivo pero no lo encuentro
[33md10eca1[m controlo opcion de transferencia cuando solo existe una cuenta
[33m90dad78[m controlo nullpointerexception en barfragment si no hay cuentas
[33me57f418[m solucionado el problema del nullPointerException
[33mb62e2c6[m refactorizo MainActivity
[33m9634796[m elimino condicional en mainactivity que no permitia abrir opcion de transaccion con una cuenta
[33m5da0b5c[m resuelvo la primera que el primer listado te lo  muestre con la moneda
[33m915b8fb[m y hasta aqui llego
[33ma80f9c1[m creo funcionalidad borrar todas las cuentas
[33m4f79de2[m actualizo en tiempo real cuentas al usar opciones dbFragment
[33m6a1ca42[m verificacion de existencia de cuenta con un iban determinado
[33md97bb2a[m refactorizacion de funciones creadas en DBFragment
[33m192e2c0[m realizadas las 4 primeras funcionalidades de DBFragment
[33mcbf932d[m simplifico las bases de datos a solo tres tablas :usuario,cuenta y movimiento..e implemento funcionalidad de borrar cuenta
[33mf8e6296[m implemento funcion borrar cuenta..pero no borra los datos de ingresos y gastos...?
[33ma94a8bf[m funcionalidad añadir cuenta creada
[33mf0d2fac[m creacion de layout de alert dialog con dos campos
[33mbb6e319[m Merge branch 'feature/db'
[33m355b195[m fin de personalizacion de alertDialog en InfoFragment
[33m34ea2e5[m espera que voy a buscar el buttonstyledialog
[33m3ee31ae[m espera que voy a buscar el custom_dialog
[33mf35997a[m funcionalida añadir cuenta realizada
[33mfdcfb2d[m creacion del layout de operaciones de base de datos
[33m1b9e803[m creada la vista de opcion de operaciones  de base de datos
[33mef56bd0[m Merge branch 'develop'
[33m9d5fdae[m primera fusion de esta rama
[33m18f4f40[m consigo cambiar imagen de perfil ,cogiendo una imagen de mi galeria de fotos. Genial!!
[33mfaf2199[m seleccciono imagen desde galeria
[33m981b2dd[m mejora en presentacion de foto de perfil
[33mb2dad11[m mejoras en presentacion de resultados con colores que constrastan en funcion del tema elegido
[33mca5531a[m mejoras en presentacion de color de texto en la presentacion del listado de movimientos
[33m1a5e19e[m spinner de consultas con cuentas multiples
[33m275f7c8[m deshabilito opcion de transferencia para una sola cuenta
[33m0959802[m spinner de barchartFragment con multiples cuentas
[33m12ed84f[m spinner con opciones de cuentas multiples
[33m05ff44f[m modifico activity crear cuenta para que se puedan crear de 1 a varias cuentas
[33m7eade27[m borro los fragments saldo.Ahora el saldo se mostrara como un recycler view
[33m6977c83[m muestro saldos con recyclerView
[33me91576f[m Creacion de clase AdapterBal para mostrar saldos en recycler view
[33me7459f0[m Merge branch 'develop'
[33m6686912[m solucionado problema al actualizar dni-ver comentario
[33m7471b7c[m cambio de configuracion de profile
[33m3491d09[m mejoras en presentacion de colores tema oscuro y claro
[33m3561387[m informacion de la cuenta presentada
[33ma2156e2[m diseño de layout de profile
[33m4c7b82b[m creacion de layout de profile y estilos
[33m6b9d6cc[m fin de cambio de configuracion de colores
[33mc26ae9f[m cambio de configuracion de colores de tema claro y oscuro
[33m2bf05b2[m los iconos de about cambian de color en funcion del tema
[33m258334f[m finalizacion de apartado acerca de
[33ma82fa8b[m atribuciones a google fonts y modificacion de presentacion de atribuciones de iconos
[33m7df2846[m Merge branch 'develop'
[33m8241c56[m preparado para merge
[33m00d2207[m pagina about terminada
[33md2c3d1c[m creacion de la pagina about
[33m04ffd94[m correccion de ajustes fragment
[33m34d39ea[m mejoras en fragment de ajustes
[33m8521b03[m corrijo error de fondo en previewImage
[33m89f13e8[m corrijo error en color preview Image
[33m556a0d9[m restablezco los Toast
[33m67c973a[m mensajes de advertencias en campos en lugar de Toast
[33m0382ad1[m implemento icono de idiomas
[33m0ea0d84[m implemento interfaz OnLocaleListener que comunica adapter con fragment listOfMov para locale y mostrar items con la moneda seleccionada en ajustes
[33m71a8f04[m modificacion de iconos y preparado para merge con main
[33m3b561f1[m creacion de fragment de about y modificacion de iconos en menu lateral
[33m8a5536d[m refactorizacion
[33m1428e5f[m refactorizacion de codigo
[33m93d3235[m resolcion de confictos con rama feature/refactor
[33m98e2faf[m fin de refactorizacion de codigo
[33m72932c4[m configuracion de formato de numero europeo sea cual sea la configuracion de idioma
[33m92014a6[m refactorizo codigo
[33m9c4e88c[m Creacion de interface y migracion de funciones a Utils
[33m6fdfe2e[m creacion de Object para agrupar constantes de calculator
[33m207c28d[m correccion de error en busqueda por importe. Cambio de codigo en clase Adapter
[33mcf90859[m Solucionado problema de presentacion de barCharts
[33m5070a63[m ajustes en manifest para nombre de app en funcion del idioma y cambios en iconos de app
[33m5a8b435[m refactorizacion codigo
[33mc8eebc4[m refactorizo funciones getOperator y result
[33m2130bbb[m  preparados para implementar por pantalla 1.000
[33mef183d7[m formato a porcentajes
[33m1eb1b20[m por fin consigo la calculadora que funcione bien con un formato de tipo #,###.##
[33mb4611a6[m refactorizo codigo
[33ma44c9a6[m mejoras en formato de calculadora
[33m3295dca[m corrijo errores en funcion onCLick
[33m0b5d1c6[m estilos de fragment consulta
[33m8cb172a[m corrijo errores en vistas horizontales de entrada de datos
[33me412368[m defino estilos de fragments de transaction
[33mbf7d846[m defino estilos de fragments new amount
[33m8d3da1b[m definicion de estilos de botones y campos de entrada
[33md2ee6e0[m Comienzo definicion de styles para componentes
[33m46c14a3[m Definicion de styles para los botones y textview de calculadora
[33m0a6dff6[m funcionalidad de calculadora acabada y fusionada con feature/calc
[33m1fb7e05[m funcionalidad de calculadora acabada
[33m7459fee[m ajuste en colores de activities
[33mc281089[m correccion de colores de fondo en app
[33mace7522[m funcionalidad de calculadora terminada
[33mc3cae92[m mejora de funcionalidad de calcululadora
[33m158955f[m funcionalidad de calculadora acabada
[33m01d7c4e[m calculadora en funcionamiento
[33m2227bb1[m añado icono para opcion calculadora
[33mda33a93[m trabajo con calculadora
[33m0b98764[m trabajando en la funcionalidad de la calculadora
[33md8ed81a[m funcionalidad del boton de retroceso y limpiar todo
[33m593e358[m correccion del layout calculator
[33m035d552[m tecla de retroceso en calculadora
[33m6cecbe0[m diseño de fragment calculator con orientacion land hecho
[33m238da0c[m diseño fragment calculator hecho
[33m31d482f[m layout de calculadora
[33mcae60c5[m comienzo diseño de fragment calculadora
[33m307475c[m documentacion de codigo con comentarios
[33m6a43871[m mejoras en colores de temas
[33m6695da3[m mejoras en AjustesFragment
[33me8198ff[m Merge branch 'main' of https://github.com/AgusCarBCN/CuentaAppAndroid
[33m77eb79f[m ready to merge
[33m33f12e0[m consigo actualizar saldo grande!!!!
[33m85eeaf1[m consigo formato de divisas
[33m0966b9a[m comienzo a probar cambio de divisas en adapter
[33mc8102e5[m consigo adelantar con la configuracion de divisas
[33m67cc0a9[m configuro pantalla de ajustes vista completa.idioma y tema funcionan
[33mcfd4158[m termino de poner fragment de ajustes
[33m139832d[m ajustes para camnbio de idioma español ingles
[33m1a9342a[m pagina de home hecha
[33m0df7bdd[m mejoras en apariencia de tema claro
[33m8f8c926[m corrijo pequeño fallo en infocontainer
[33mf848daa[m añado iconos a la funcion cambiar tema
[33m8ae70fb[m ajustes fuerzo el modo oscuro en app
[33medbd5ad[m definicion de item de navigationview
[33mf73b5be[m configuracion de tema oscuro y tema claro en app
[33m2bba4c7[m cambio menu de ajustes por navigationview
[33m4a52e59[m ajustes en menu MainActivity para esconder saldo cuando se selecciones settings
[33m02ba8c5[m Merge branch 'develop'
[33m46a1b54[m creacion de activity para contraseña olvidada
[33meb0dab0[m diseño de la activity para crear nueva contraseña
[33m5e558b9[m cambio de sharedpreferences
[33m4913f3d[m ok
[33m66a7cd3[m cambio campo de base de datos de telefono a email
[33m1225f47[m commit
[33md614c4e[m refactorizacion de codigo
[33m0b05fa1[m Corrijo error de busqueda por texto
[33mac3ae23[m  sonidos conseguidos
[33m8e52656[m sonido al clickear botones
[33md1e9ce5[m listo para merge. Mejora efecto de botones al presionar
[33m5a91db8[m creo un efecto visual en los botones cuando se presionen
[33m6aa7bbd[m ajustes en app
[33mac9998f[m elimina opcion crear nuevo usuario si ya existe un usuario
[33md2e8c4a[m refactorizo un poco el codigo
[33md6018fd[m fin de implantacion de viewBinding
[33m17b21bf[m Implementacion de viewBinding en activity createuseractivity
[33m44f6527[m implementacion viewBinding en ConsultaFragment
[33m063d15e[m Implementacion de viewBinding en TransacctionFragment
[33mf3d9508[m Implemetancion de viewBinding en NewAmountFragment
[33mc1342a7[m cambio de acceso a vistas con en activity Login viewBinding
[33mbc2fbb0[m preparado para merge
[33ma3c660e[m sigo con las mejoras en el fichero string.xml
[33mecde7e6[m acabo mejoras en traduccion español e ingles
[33m5b04ae0[m mejora en fragment nuevo importe.Ahora borra campos al añadir un importe
[33mea209c8[m configuracion de string.xml
[33mbd15d2b[m mejora de string hasta consulta
[33mb65660a[m configuracion de fichero strings.xml para favorecer la traduccion a español e ingles
[33ma332a75[m aplico dimensiones del archivo dimen.xml que creo para especificar dimensiones
[33m5df91b5[m cambio en barcharFragment
[33m9538128[m uso de synchronized en clase DatabaseSingleton
[33m37a304e[m cambio de estilo radiobuttons
[33m8c3ecc6[m resuelvo problema de formato de fechas y consigo importar archivo csv
[33m4417f68[m Modificacion del adapter permitiendo visualizar los movimientos de manera mas compacta
[33m5579451[m he conseguido volcar datos pero tengo inconsitencias.problema con adapter al mostra como quisiera y al buscar por fecha
[33ma82deee[m primeras pruebas para leer archivo cvs
[33mdc0d994[m corrijo error en fragment de saldo y consultas
[33ma0fdb69[m Merge pull request #1 from AgusCarBCN/develop
[33m80e1ccf[m Añado Resultado de las busquedas y mejoro formato de saldos y resultados
[33m63bf943[m Cambio de formato de items de recyclerview a mas amigable
[33md4097d0[m preparado para merge a main
[33m13880e6[m Acabo de ajustar los layouts
[33m9812437[m corrijo errores de scroll,cambio fondo y color barras
[33m3d3289d[m correccion color de fondo en landscape layout de login
[33mf662757[m Correcion de los errores encontrados.Listo para merge con develop
[33m5bc021a[m verificar que los campos de importe y descripcion no esten vacios en nuevo importe
[33m4610aba[m cambio de color de fondo graficos a gris muy muy claro
[33m7192dab[m Cambio del color de barras a colores mas mates
[33ma4c21d4[m centro logo
[33m03494cd[m cambio de color naranja a otro menos intenso y mas pastel
[33m5c26b54[m Corregido error al mostrar graficos
[33m812797a[m Actualizo de manera automatica los graficos al seleccionar las opciones de los spinner
[33m2c6f3bc[m Refactorizo codigo en funciones
[33m066e284[m Creacion de graficos con los importes de los movimientos de la base de datos
[33me2a99a6[m codigo con listener de los spinner y primeras pruebas para hacer los calculos de montantes por mes
[33me3a9e88[m Preparado para introducir datos reales
[33m858beba[m Consigo darle color a las tres barras con propiedad Color
[33m7bdbaec[m Consigo centrar los grupos de barras con las etiquetas y crear todos los grupos por meses
[33m293d140[m Primera prueba con barChart
[33m98f5f69[m creacion de spinner con su funcionalidad
[33m28275d6[m Incluyo clase calculos para realizar los calculos de los gastos e ingresos al mes
[33me307abe[m Mejoras en presentacion de opciones en spinner y personalizacion de estilo
[33m5d3233f[m Corrijo error en fragment_create_user..
[33m37a8439[m Listo para merge
[33mb00b560[m Arreglo pequeño error en los radio button..ahora permite cambiar de radio button mostrando un resultado si cambias antes de pulsar aceptar
[33m659e801[m Corrijo advertencias moderadas
[33mc02fd3c[m Validacion de fechas e importes validos
[33md7d84ef[m Filtro de fecha logrado
[33m2e07c6e[m correccion de errores leves y moderados en codigo
[33m27b1e17[m suprimo clase movimientoDaoProxy,no era necesario
[33m18b660b[m Consigo filtrar por texto los importes
[33m8d6ffa4[m Filtro de fechas conseguido
[33maa30b43[m Consigo aplicar filtro por importes con exito
[33m34d77a6[m convierto algunas variables en globales
[33mc1ae194[m corrijo consultaFragment
[33m2cfe299[m corrijo entrada de importe en transacciones para que solo admita numeros
[33me0ff9bb[m intento de crear spinner personalizado
[33mc84d129[m Listado de importes por cuenta conseguido. Añado un hint casero al spinner spConsulta
[33mb5560cb[m muestro resultados por iban
[33m938ccef[m corrijo error en radio button ingreso que se quedaba seleccionado por defecto
[33m4444d86[m subo a nivel 33 y asi evito metodo obsoleto getParcelableArrayList
[33ma0edde4[m Se consigue pasar un arrayList entre el fragmento consulta y el fragmento listOfMov.Funcionalidad de selecciones en radio button perfecta
[33m48b320a[m funcionalidad de selecciones
[33m6b96b64[m Implementacion del boton aceptar de consulta para llegar a fragment de listado de movimientos.mejoro presentacion de items
[33mbca62c3[m version develop
[33m82169ba[m implemento interfaz Parcelable en MovimientoBancario
[33m47ffe0e[m cambio de formato de presentacion de fecha
[33ma8212a3[m Mejora en layout de consulta
[33mb944203[m primer merge
[33md3830b0[m implementacion de patron proxy para movimientosdao
[33mf79b7d2[m fragment ConsultaFragment preparado para recibir datos
[33mb4fbd60[m Personalizacion de los colores del datepicker dialog
[33m7181e97[m implementacion de colores de app en archivo colors.xml.Creacion de fragment de consultas
[33m63eeb67[m Aplico tamaño a ListOfMovFragment de 400 dp para que no ocupe toda la pantalla
[33m68d2b27[m Mejoras en presentacin de datos de recyclerview y consigo solucionar problemas de scroll
[33m7d4648c[m mejoro presentacion de importes.Los gastos aparecen en numeros rojos y los ingresos en verdes
[33m2f141af[m Funcionalidad de consulta.falta mejorar vista
[33maa74929[m mejoras y cambios en presentacion de spinner
[33m370217c[m añado nombre del usuario logeado a la pagina de inicio
[33m897997c[m Uso de patron Singleton para DataBaseApp usando la app de Kotlin
[33ma9b18c3[m funcionalidad de transferencia realizada con exito
[33m0471229[m Cargo datos de cuenta en spinner.Pantalla lista para recibir datos
[33mf1bb62e[m Uso de funciones de cuentaDao para obtener cuentas por dni
[33m2a44caf[m Login usando la funcion que proporciona UsuarioDAO
[33m21aa483[m funcionalidad completa
[33m7591196[m preparado  para fusionar
[33me4cd6b2[m Actualizo saldos en tiempo real
[33ma133bc7[m Mejora en la presentacion de fragments y layouts en general
[33m2539271[m Funcionalidad completa de los daos,y fragment principal mostrando saldos
[33m39f6d36[m creo MovimientoBancarioDao
[33m4a4c3e2[m consigo seleccionar item de spinner
[33m43e4fd8[m cambio en MovimientoBancario
[33m2c37487[m funcionalidad de DAOs perfecta en createUser
[33m9a49ddc[m creacion de clases dao
[33m7acac47[m mejora de vista crear cuenta y creacion de clases modelo
[33maec8913[m cambio fragment de crear usuario y creo una actividad CreateUserActivity en su lugar. diseño de vistas en fragments para las orientaciones
[33m5c2a8dd[m corrijo error en boton de crear nuevo usuario para entrar
[33m77d75ee[m refactorizacion de funcion login para inicio de sesion
[33m4ddc5a0[m añado opciones al spinner cuentas desde base de datos CUENTA
[33m7f43f76[m Uso de SharedPreferences para almacenar y recuperar el dni del usuario que inicia sesion
[33m0e88f13[m Corrigo base de datos USUARIO y hago ajustes en fragment CreateUserFragment
[33me36797b[m trigger implementado con exito
[33m8c7c268[m Inicio de sesion con SQLite implementado
[33mbee4140[m Inserto usuario y cuentas de usuario desde fragment createuser
[33m2813c5b[m acceso a edittext y botones de createuserfragment. creacion de funcionalidad en boton cancel
[33m0ac2a52[m mejoras en fragment de crear cuenta
[33macaa131[m Añado fragment para crear nuevo usuario. Mejoras en formato de botones y edit text
[33m7adcd5f[m ajuste de botones en activities de creacion de usuario y cuentas
[33m08c3d3e[m Creacion de activities para crear nuevo usuario
[33m92e9afb[m primeros fragments
[33m6c7066a[m editText en gravity center
[33m7e6ba94[m Añadir funcionalidad de traducir aplicacion español /ingles
[33m4a1e549[m Cambio a LinearLayout en activities de Login
[33m01caea1[m Mejora del login y primera pantalla emergente de nuevo importe
[33m84a0230[m CuentaApp para Android .Acceso a menu funciones y login de app
[33m9580f63[m Inicio de proyecto CuentaApp en android studio
