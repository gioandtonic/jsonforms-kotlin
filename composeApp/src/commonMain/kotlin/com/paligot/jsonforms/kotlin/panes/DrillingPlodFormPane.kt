package com.paligot.jsonforms.kotlin.panes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.jsonforms.kotlin.models.schema.NumberProperty
import com.paligot.jsonforms.kotlin.models.schema.Schema
import com.paligot.jsonforms.kotlin.models.schema.StringProperty
import com.paligot.jsonforms.kotlin.models.uischema.Control
import com.paligot.jsonforms.kotlin.models.uischema.VerticalLayout
import com.paligot.jsonforms.kotlin.ui.FormScaffold
import com.paligot.jsonforms.material3.Material3Layout
import com.paligot.jsonforms.material3.Material3NumberProperty
import com.paligot.jsonforms.material3.Material3StringProperty
import com.paligot.jsonforms.ui.JsonForm
import com.paligot.jsonforms.ui.rememberJsonFormState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

@Composable
fun DrillingPlodFormPane(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberJsonFormState(initialValues = mutableMapOf(
        "#/properties/rigId" to "RIG-SURFACE-101",
        "#/properties/drillerName" to "John Doe",
        "#/properties/startDepth" to "100.0",
        "#/properties/endDepth" to "145.5",
    ))

    val schema = remember {
        Schema(
            properties = persistentMapOf(
                "rigId" to StringProperty(),
                "drillerName" to StringProperty(),
                "startDepth" to NumberProperty(minimum = 0),
                "endDepth" to NumberProperty(minimum = 0),
                "lithology" to StringProperty(),
                "drillerNotes" to StringProperty(),
            ),
            required = persistentListOf("rigId", "drillerName", "startDepth", "endDepth"),
        )
    }

    val uiSchema = remember {
        VerticalLayout(
            elements = persistentListOf(
                Control(
                    scope = "#/properties/rigId",
                    label = "Rig Identifier",
                ),
                Control(
                    scope = "#/properties/drillerName",
                    label = "Driller In-Charge",
                ),
                Control(
                    scope = "#/properties/startDepth",
                    label = "Start Depth (meters)",
                ),
                Control(
                    scope = "#/properties/endDepth",
                    label = "End Depth (meters)",
                ),
                Control(
                    scope = "#/properties/lithology",
                    label = "Primary Lithology Code",
                ),
                Control(
                    scope = "#/properties/drillerNotes",
                    label = "Shift Operational Notes",
                ),
            ),
        )
    }

    FormScaffold(
        title = "StrataOre Drilling PLOD Preview",
        modifier = modifier,
        onBackClick = onBackClick,
    ) {
        Column(modifier = Modifier.width(500.dp).padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Drilling Shift Capture (JSONForms Showcase)")
                }
            }

            JsonForm(
                schema = schema,
                uiSchema = uiSchema,
                state = state,
                layoutContent = { Material3Layout(content = it) },
                stringContent = { id ->
                    val value = state[id].value as String?
                    val error = state.error(id = id).value
                    Material3StringProperty(
                        value = value,
                        error = error?.message,
                        onValueChange = {
                            state[id] = it
                        },
                    )
                },
                numberContent = { id ->
                    val value = state[id].value as String?
                    val error = state.error(id = id).value
                    Material3NumberProperty(
                        value = value,
                        error = error?.message,
                        onValueChange = {
                            state[id] = it
                        },
                    )
                },
                booleanContent = {},
            )

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    scope.launch { state.validate(schema, uiSchema) }
                }
            ) {
                Text("Validate Shift Capture")
            }
        }
    }
}
