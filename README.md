# My Accounts

An Android application for personal financial management with support for multiple accounts and detailed expense control.

## 📱 Description

**My Accounts** is an application designed to simplify the management of your income and expenses. With this tool, you can efficiently handle transactions for one to several accounts, add income, record expenses, transfer funds between accounts, and generate detailed statistics through transaction searches.

The application also features a handy integrated calculator, allowing you to perform calculations quickly and easily without leaving the app.

## ✨ Key Features

### 💳 Account Management
- Support for multiple accounts with different currencies
- Automatic currency conversion with real-time API
- Transfers between accounts
- Expense limits per account
- Balance control

### 📊 Financial Control
- Detailed income and expense recording
- Automatic transaction categorization
- Advanced search by date, amount, and description
- Modification and deletion of records

### 📈 Statistics & Analytics
- Bar charts: monthly income, expenses, and results
- Pie charts: expense distribution by category
- Spending limit control
- Notification alerts when limits are exceeded

### 🎨 Interface & Experience
- Configurable light/dark theme
- Interactive onboarding for new users
- User profile with photo and personalized data
- Intuitive navigation with side menu

### 🔧 Additional Tools
- Integrated calculator
- Backup/restore system in CSV format
- Notification management
- Multi-language support (Spanish, English)

## 🛠️ Technologies Used

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Database**: Room
- **Navigation**: Navigation Compose
- **Charts**: MPAndroidChart
- **Networking**: Retrofit + Gson
- **Currency**: Exchangerate-API
- **Ads**: Google AdMob

## 📋 Requirements

- Android 5.0 (API level 26) or higher
- Internet connection for currency conversion

## 🚀 Installation

1. Clone the repository:
```bash
git clone https://github.com/AgusCarBCN/MisCuentasApp.git
```

2. Open the project in Android Studio
3. Configure your `secrets.properties` with required API keys:
   - Exchangerate-API key
   - AdMob key

4. Sync the project and run

## 📸 Screenshots

*(Add screenshots when available)*

## 🏗️ Project Structure

```
app/
├── src/main/java/carnerero/agustin/cuentaappandroid/
│   ├── data/                 # Data layer
│   │   ├── db/              # Room database
│   │   ├── network/         # API and network clients
│   │   ├── repository/      # Repository implementations
│   │   └── pref/           # DataStore and preferences
│   ├── domain/              # Domain layer
│   │   ├── database/        # Database use cases
│   │   ├── apidata/         # API use cases
│   │   └── datastore/      # Preference use cases
│   ├── presentation/        # Presentation layer
│   │   ├── ui/             # UI components and screens
│   │   ├── common/         # Shared components
│   │   └── navigation/     # Navigation
│   └── utils/               # Utilities and extensions
├── src/main/res/            # Android resources
└── src/test/               # Unit tests
```

## 🤝 Contributing

Contributions are welcome. Please follow these steps:

1. Fork the project
2. Create a feature branch (`git checkout -b feature/NewFeature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/NewFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License. See the [`LICENSE`](LICENSE) file for more details.

### License Summary
✅ **Allowed:**
- Commercial use
- Modification
- Distribution
- Private use
- Sublicensing

❌ **Required:**
- Include license and copyright
- Provide link to original repository

🚫 **No warranty:** Software is provided "as is"

## 📞 Contact

- **Developer**: Agustin Carnerero Peña
- **Email**: agusticar@gmail.com
- **GitHub**: https://github.com/AgusCarBCN

## 🙏 Acknowledgments

- Accounting icons by 2D3ds (Flaticon)
- Google Fonts icons
- Currency API: Exchangerate-API
- Flag icons: Flagpedia.net
- Additional icons: Uxwing icons

## 📝 Version Notes

### Version 4.2
- UI improvements
- Performance optimization
- New expense categories
- Notification enhancements

---

**My Accounts** - Your personal finance tool on Android 🚀

## 🌍 Other Languages

- [Español](README.es.md) - Spanish version