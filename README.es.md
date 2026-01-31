# Mis Cuentas App

Una aplicación Android para la gestión financiera personal con soporte para múltiples cuentas y control de gastos detallado.

## 📱 Descripción

**My Accounts** es una aplicación diseñada para simplificar la gestión de tus ingresos y gastos. Con esta herramienta, puedes manejar eficientemente transacciones para una o varias cuentas, agregar ingresos, registrar gastos, transferir fondos entre cuentas y generar estadísticas detalladas mediante búsquedas de transacciones.

La aplicación también cuenta con una práctica calculadora integrada, permitiéndote realizar cálculos rápidamente sin salir de la aplicación.

## ✨ Características Principales

### 💳 Gestión de Cuentas
- Soporte para múltiples cuentas con diferentes monedas
- Conversión automática de divisas con API en tiempo real
- Transferencias entre cuentas
- Límites de gasto por cuenta
- Control de saldo

### 📊 Control Financiero
- Registro detallado de ingresos y gastos
- Categorización automática de transacciones
- Búsqueda avanzada de registros por fecha, monto y descripción
- Modificación y eliminación de registros

### 📈 Estadísticas y Análisis
- Gráficos de barras: ingresos, gastos y resultados mensuales
- Gráficos circulares: distribución de gastos por categoría
- Control de límites de gasto
- Notificaciones de alerta cuando se exceden los límites

### 🎨 Interfaz y Experiencia
- Tema claro/oscuro configurable
- Onboarding interactivo para nuevos usuarios
- Perfil de usuario con foto y datos personalizados
- Navegación intuitiva con menú lateral

### 🔧 Herramientas Adicionales
- Calculadora integrada
- Sistema de backup/restore en formato CSV
- Gestión de notificaciones
- Soporte multiidioma (Español, Inglés)

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM + Clean Architecture
- **Inyección de Dependencias**: Dagger Hilt
- **Base de Datos**: Room
- **Navegación**: Navigation Compose
- **Gráficos**: MPAndroidChart
- **Red**: Retrofit + Gson
- **Monedas**: Exchangerate-API
- **Anuncios**: Google AdMob

## 📋 Requisitos

- Android 5.0 (API level 26) o superior
- Conexión a internet para conversión de divisas

## 🚀 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/AgusCarBCN/MisCuentasAppBigdecimal.git
```

2. Abre el proyecto en Android Studio
3. Configura tu `secrets.properties` con las claves API necesarias:
   - Clave de Exchangerate-API
   - Clave de AdMob

4. Sincroniza el proyecto y ejecuta

## 📸 Capturas de Pantalla

*(Agrega capturas de pantalla cuando estén disponibles)*

## 🏗️ Estructura del Proyecto

```
app/
├── src/main/java/carnerero/agustin/cuentaappandroid/
│   ├── data/                 # Capa de datos
│   │   ├── db/              # Base de datos Room
│   │   ├── network/         # API y clientes de red
│   │   ├── repository/      # Implementaciones de repositorios
│   │   └── pref/           # DataStore y preferencias
│   ├── domain/              # Capa de dominio
│   │   ├── database/        # Casos de uso de base de datos
│   │   ├── apidata/         # Casos de uso de API
│   │   └── datastore/      # Casos de uso de preferencias
│   ├── presentation/        # Capa de presentación
│   │   ├── ui/             # UI components y screens
│   │   ├── common/         # Componentes compartidos
│   │   └── navigation/     # Navegación
│   └── utils/               # Utilidades y extensiones
├── src/main/res/            # Recursos Android
└── src/test/               # Pruebas unitarias
```

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor, sigue estos pasos:

1. Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit de tus cambios (`git commit -m 'Agrega nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [`LICENSE`](LICENSE) para más detalles.

### Resumen de la Licencia
✅ **Permitido:**
- Uso comercial
- Modificación
- Distribución
- Uso privado
- Sublicenciamiento

❌ **Requerido:**
- Incluir licencia y copyright
- Proporcionar enlace al repositorio original

🚫 **Sin garantía:** El software se proporciona "tal como está"

## 📞 Contacto

- **Desarrollador**: Agustin Carnerero Peña
- **Email**: agusticar@gmail.com
- **GitHub**: https://github.com/AgusCarBCN

## 🙏 Agradecimientos

- Iconos de contabilidad por 2D3ds (Flaticon)
- Iconos de Google Fonts
- API de divisas: Exchangerate-API
- Iconos de banderas: Flagpedia.net
- Iconos adicionales: Uxwing icons

## 📝 Notas de la Versión

### Versión 5.0
- Mejoras en la interfaz de usuario
- Optimización del rendimiento
- Nuevas categorías de gastos
- Mejoras en las notificaciones

---

**My Accounts** - Tu herramienta financiera personal en Android 🚀

## 🌍 Other Languages

- [English](README.md) - Versión en inglés