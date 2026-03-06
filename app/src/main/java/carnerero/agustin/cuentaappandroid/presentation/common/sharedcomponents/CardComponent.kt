package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.utils.Utils


@Composable

fun UserImage(uri: Uri, size: Int) {
    val profileImage= stringResource(id = R.string.profileimage)
    Card(
        modifier = Modifier
            .size(size.dp)
            .padding(dimens.medium),
        shape = CircleShape
    )

    {
        Image(
            painter = if (uri == Uri.EMPTY) painterResource(id = R.drawable.contabilidad)
            else rememberAsyncImagePainter(uri), // Carga la imagen desde el Uri ,
            contentDescription = profileImage,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun HeadCard(modifier: Modifier, amount: String, isIncome: Boolean, onClickCard: () -> Unit) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimens.small
        ),
        colors = CardColors(
            containerColor = colors.drawerColor,
            contentColor = if (isIncome) colors.incomeColor else colors.expenseColor,
            disabledContainerColor = colors.drawerColor,
            disabledContentColor = colors.incomeColor

        ),
        modifier =modifier
    ) {
        Text(
            text = amount,
            modifier = Modifier
                .padding(top = dimens.mediumLarge)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style=MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        TextButton(
            onClick = {
                onClickCard()
            },
            content = {
                Text(
                    modifier = Modifier
                        .padding(dimens.small)
                        .fillMaxWidth(),
                    text = stringResource(id = if (isIncome) R.string.seeincome else R.string.seeexpense),
                    textAlign = TextAlign.Center,
                    color = colors.textColor,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
fun AccountCard(
    account: Account,
    currencyCode: String,
    textButton: Int,
    onClickCard: () -> Unit,
    modifier: Modifier
) {
    val contentText=stringResource(textButton)
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimens.small
        ),
        colors = CardColors(
            containerColor = colors.drawerColor,
            contentColor = colors.incomeColor,
            disabledContainerColor = colors.drawerColor,
            disabledContentColor = colors.incomeColor

        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)   // Altura mínima
            .widthIn(max = 420.dp)    // Limita ancho máximo en tablets
    ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.smallMedium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimens.extraLarge),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = account.name,
                        modifier = Modifier.weight(0.6f),
                        textAlign = TextAlign.Start,
                        color = colors.textColor,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = Utils.numberFormat(account.balance, currencyCode),
                        modifier = Modifier.weight(0.4f),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(dimens.smallMedium))

                TextButton(
                    onClick = onClickCard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = contentText,
                        textAlign = TextAlign.End,
                        color = colors.textColor,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "$contentText ${account.name}"
                        }
                    )
                }
            }
        }
    }


