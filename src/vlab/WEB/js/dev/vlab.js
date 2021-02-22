function renderTemplate(element, html) {
    element.innerHTML = html;
}

function zeros(dimensions) {
    let array = [];

    for (let i = 0; i < dimensions[0]; ++i) {
        array.push(dimensions.length == 1 ? 0 : zeros(dimensions.slice(1)));
    }

    return array;
}

function getHTML(templateData)
{
    console.log("template", templateData);
    let initialMatrixTable = ``;
    let initialMatrix = [...templateData.initialMatrix];
    let compositionMatrix = [];

    let compositionMatrixColumnsInput = `<input id="compositionMatrixColumns" type="number" ${templateData.isCompositionMatrixCreated ? "disabled" : ""}/>`;
    let compositionMatrixRowsInput = `<input id="compositionMatrixRows" type="number" ${templateData.isCompositionMatrixCreated ? "disabled" : ""}/>`;
    let compositionMatrixTable = ``;

    let significanceMatrixApplyInput = ``;
    let compositionMatrixCancelInput = ``;
    let significanceMatrixContainer = ``;

    if(initialMatrix.length)
    {
        initialMatrixTable += `<table class="R1SetTable">`;

        initialMatrixTable += `<tr>`;

        for(let i = 0; i < initialMatrix.length + 1; i++)
        {
            if(i === 0)
                initialMatrixTable += `<td> </td>`;
            else
                initialMatrixTable += `<td class="initialMatrixBg">${i}</td>`;
        }

        initialMatrixTable += `</tr>`;

        for(let i = 0; i < initialMatrix.length; i++)
        {
            initialMatrixTable += `<tr>`;

            for(let j = 0; j < initialMatrix[i].length; j++)
            {
                if(j === 0)
                    initialMatrixTable += `<td class="initialMatrixBg">${i + 1}</td>`;

                initialMatrixTable += `<td>${initialMatrix[i][j]}</td>`;
            }

            initialMatrixTable += `</tr>`;
        }

        initialMatrixTable += `</table>`;
    }

    if(templateData.isCompositionMatrixSizeCreated)
    {
        compositionMatrixColumnsInput = `<input id="compositionMatrixColumns" type="number" ${templateData.isCompositionMatrixSizeCreated ? "disabled" : ""} value="${templateData.isCompositionMatrixSizeCreated ? templateData.compositionMatrixColumns : ""}"/>`;
        compositionMatrixRowsInput = `<input id="compositionMatrixRows" type="number" ${templateData.isCompositionMatrixSizeCreated ? "disabled" : ""} value="${templateData.isCompositionMatrixSizeCreated ? templateData.compositionMatrixRows : ""}"/>`;
        significanceMatrixApplyInput = `<input class="btn btn-secondary" id="significanceMatrixApply" type="button" value="Далее" ${templateData.isCompositionMatrixCreated ? "disabled" : ""}/>`;
        compositionMatrixCancelInput = `<input class="btn btn-danger" type="button" id="cancelCompositionMatrix" value="Назад" ${templateData.isCompositionMatrixCreated ? "disabled " : ""}/>`;

        compositionMatrix = templateData.compositionMatrix.slice();
        compositionMatrixTable += `<table class="compositionMatrixTable_values_table">`;

        compositionMatrixTable += `<tr>`;

        for(let i = 0; i < compositionMatrix.length + 1; i++)
        {
            if(i === 0)
                compositionMatrixTable += `<td> </td>`;
            else
                compositionMatrixTable += `<td class="initialMatrixBg">${i}</td>`;
        }

        compositionMatrixTable += `</tr>`;

        for(let i = 0; i < compositionMatrix.length; i++)
        {
            compositionMatrixTable += `<tr>`;
            for(let j = 0; j < compositionMatrix[i].length; j++)
            {
                if(j === 0)
                    compositionMatrixTable += `<td class="initialMatrixBg">${i + 1}</td>`;

                compositionMatrixTable += `<td><input type="number" id="compositionMatrixInput_${i}_${j}" value="${compositionMatrix[i][j]}" ${templateData.isCompositionMatrixCreated? "disabled" : ""}/></td>`;
            }

            compositionMatrixTable += `</tr>`;
        }

        compositionMatrixTable += `</table>`;
    }

    if(templateData.isCompositionMatrixCreated)
    {
        let significanceMatrixTable = `<table class="R1SetTable">`
        let significanceMatrix = [...templateData.significanceMatrix];
        let tranzitionMatrix = [...templateData.tranzitionMatrix];

        significanceMatrixTable += `<tr>`;

        for(let i = 0; i < significanceMatrix.length + 1; i++)
        {
            if(i === 0)
                significanceMatrixTable += `<td> </td>`;
            else
                significanceMatrixTable += `<td class="initialMatrixBg">${i}</td>`;
        }

        significanceMatrixTable += `</tr>`;

        for(let i = 0; i < significanceMatrix.length; i++)
        {
            significanceMatrixTable += `<tr>`;

            for(let j = 0; j < significanceMatrix[i].length; j++)
            {
                if(j === 0)
                    significanceMatrixTable += `<td class="initialMatrixBg">${i + 1}</td>`;

                significanceMatrixTable += `<td class="tranzitionTable_td ${tranzitionMatrix[i][j] === 1 ? "tranzitionMatrixElement_yellow" : ""}" id="significanceMatrixInputId_${i}_${j}">${significanceMatrix[i][j]}</td>`;
            }

            significanceMatrixTable += `</tr>`;
        }

        significanceMatrixTable += `</table>`

        significanceMatrixContainer = `
        <div class="significanceMatrixContainer">
            <h2>Проверка условия транзитивности:</h2>
            ${significanceMatrixTable}
            <input class="btn btn-danger" type="button" id="cancelSignificanceMatrix" value="Назад"/>
        </div>`;
    }

    return `
        <div class="lab">
            <table class="lab-table">
                <tr>
                    <td>
                        <div class="lab-header">                          
                            <span class="lab-header_name">Транзитивность нечетких отношений</span>
                            <!-- Button trigger modal -->
                            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#exampleModalScrollable">
                              Справка
                            </button>
                                                                                
                            <!-- Modal -->
                            <div class="modal fade" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
                              <div class="modal-dialog modal-dialog-scrollable" role="document">
                                <div class="modal-content">
                                  <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalScrollableTitle">Справка по интерфейсу лабораторной работы</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                      <span aria-hidden="true">&times;</span>
                                    </button>
                                  </div>
                                  <div class="modal-body">
                                       <p>Введите количество строк и столбцов в матрице отношения второй степени и и нажмите кнопку <b>«Создать матрицу»</b>. После этой команды эта матрица будет доступна для заполнения. Если она создана неправильно, то нажмите кнопку <b>«Назад»</b> и исправьте размерность матрицы.</p>
                                       <p>Если матрица отношения второй степени заполнена, то нажмите кнопку <b>«Далее»</b>. После этой команды будет доступна матрица отношения второй степени для проверки транзитивности. Щелкайте по тем ячейкам этой матрицы, в которых есть нарушение условия транзитивности заданного отношения: эти ячейки будут <b>выделены цветом</b>. Если нужно внести изменения в предыдущую матрицу, то используйте кнопку <b>«Назад»</b>.</p>
                                       <p>После завершения проверки транзитивности нажмите кнопку <b>«Ответ готов»</b> в нижнем правом углу стенда.</p>                                   
                                  </div>                                 
                                </div>
                              </div>
                            </div>                           
                        </div>
                    </td>                    
                </tr>
            </table>    
            <div class="initialMatrixTableContainer">
                <h2>Исходная матрица</h2>
                <div class="initialMatrixTable">
                    <div class="initialMatrixTable_name">M<sub>R</sub> =</div>                    
                    ${initialMatrixTable}
                </div>
            </div>                        
            <div class="compositionMatrixSize">
                <div>                  
                    <h2>Ввести размерность матрицы отношения второй степени:</h2>
                    <div class="compositionMatrixSize_div">
                        ${compositionMatrixRowsInput}
                        <span>X</span>
                        ${compositionMatrixColumnsInput}
                    </div>                                                                                                          
                </div>
                <div>
                    <input class="btn btn-secondary" id="compositionMatrixApply" type="button" value="Создать матрицу" ${templateData.isCompositionMatrixCreated ? "disabled" : ""}/>                                                        
                </div>
            </div>
            <div class="compositionMatrixContainer">                                
                <div class="compositionMatrixTable">
                    ${templateData.isCompositionMatrixSizeCreated ? `<h2>Матрица отношения второй степени:</h2>` : ""}    
                    <div class="compositionMatrixTable_values">
                        <div>
                            ${templateData.isCompositionMatrixSizeCreated ? compositionMatrixTable : ""}                                                                                      
                        </div>                                                               
                    </div>    
                    <div class="compositionMatrixButtons">
                        ${significanceMatrixApplyInput}                                             
                        ${compositionMatrixCancelInput}
                    </div>                                    
                </div>                        
                ${significanceMatrixContainer} 
            </div>                                                                              
        </div>`;
}

function initState() {
    let _state = {
        initialMatrix: [],
        isCompositionMatrixCreated: false,
        compositionMatrix: [],
        significanceMatrix: [],
        tranzitionMatrix: [],
        isCompositionMatrixSizeCreated: false,
        compositionMatrixColumns: 0,
        compositionMatrixRows: 0,
    };

    return {
        getState: function () {
            return _state
        },
        updateState: function (callback) {
            _state = callback(_state);
            return _state;
        }
    }
}

function subscriber() {
    const events = {};

    return {
        subscribe: function (event, fn) {
            if (!events[event]) {
                events[event] = [fn];
            } else {
                events[event] = [fn];
            }
        },
        emit: function (event, data = undefined) {
            events[event].map(fn => data ? fn(data) : fn());
        }
    }
}

function App() {
    return {
        state: initState(),
        subscriber: subscriber(),
    }
}

function bindActionListeners(appInstance)
{
    document.getElementById("compositionMatrixApply").addEventListener('click', () => {
        const state = appInstance.state.updateState((state) => {
            let compositionMatrixColumns = parseInt(document.getElementById("compositionMatrixColumns").value);
            let compositionMatrixRows = parseInt(document.getElementById("compositionMatrixRows").value);
            let compositionMatrix = [];

            if(compositionMatrixColumns && compositionMatrixRows &&
                !isNaN(compositionMatrixColumns) && !isNaN(compositionMatrixRows)
            )
            {
                compositionMatrix = zeros([compositionMatrixRows, compositionMatrixColumns]);
            }

            return {
                ...state,
                compositionMatrix,
                isCompositionMatrixSizeCreated: true,
                compositionMatrixColumns,
                compositionMatrixRows,
            }
        });

        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });

    if(document.body.contains(document.getElementById("cancelCompositionMatrix")))
    {
        document.getElementById("cancelCompositionMatrix").addEventListener('click', () => {
            const state = appInstance.state.updateState((state) => {
                return {
                    ...state,
                    isCompositionMatrixCreated: false,
                    isCompositionMatrixSizeCreated: false,
                    compositionMatrix: [],
                }
            });

            appInstance.subscriber.emit('render', state);
        });
    }

    if(document.body.contains(document.getElementById("significanceMatrixApply")))
    {
        document.getElementById("significanceMatrixApply").addEventListener('click', () => {
            const state = appInstance.state.updateState((state) => {
                let compositionMatrix = state.compositionMatrix;
                let significanceMatrix = [...compositionMatrix];
                let tranzitionMatrix = zeros([significanceMatrix.length, significanceMatrix.length]);

                for(let i = 0; i < compositionMatrix.length; i++)
                {
                    for(let j = 0; j < compositionMatrix[i].length; j++)
                    {
                        let compositionMatrixInputId = `compositionMatrixInput_${i}_${j}`;
                        compositionMatrix[i][j] = parseFloat(document.getElementById(compositionMatrixInputId).value);
                    }
                }

                return {
                    ...state,
                    significanceMatrix,
                    compositionMatrix,
                    tranzitionMatrix,
                    isCompositionMatrixCreated: true,
                }
            });

            // перересовываем приложение
            appInstance.subscriber.emit('render', state);
        });
    }

    if(appInstance.state.getState().significanceMatrix && appInstance.state.getState().significanceMatrix.length > 0 && appInstance.state.getState().significanceMatrix[0].length > 0)
    {
        let significanceMatrix = [...appInstance.state.getState().significanceMatrix];
        let tranzitionMatrix = [...appInstance.state.getState().tranzitionMatrix];

        for(let i = 0; i < significanceMatrix.length; i++)
        {
            for(let j = 0; j < significanceMatrix[i].length; j++)
            {
                let compositionMatrixInputId = `significanceMatrixInputId_${i}_${j}`;

                document.getElementById(compositionMatrixInputId).addEventListener('click', (event) => {
                    if(tranzitionMatrix[i][j] === 0)
                    {
                        tranzitionMatrix[i][j] = 1;
                    }
                    else if(tranzitionMatrix[i][j] === 1)
                    {
                        tranzitionMatrix[i][j] = 0;
                    }

                    const state = appInstance.state.updateState((state) => {
                        return {
                            ...state,
                            tranzitionMatrix,
                        }
                    });

                    // перересовываем приложение
                    appInstance.subscriber.emit('render', state);
                });
            }
        }
    }

    if(document.body.contains(document.getElementById("cancelSignificanceMatrix")))
    {
        document.getElementById("cancelSignificanceMatrix").addEventListener('click', () => {
            const state = appInstance.state.updateState((state) => {
                return {
                    ...state,
                    isSignificanceMatrixCreated: false,
                    isCompositionMatrixCreated: false,
                    tranzitionMatrix: [],
                }
            });

            appInstance.subscriber.emit('render', state);
        });
    }
}

function init_lab() {
    const appInstance = App();
    return {
        setVariant : function(str){},
        setPreviosSolution: function(str){},
        setMode: function(str){},

        //Инициализация ВЛ
        init : function(){
            this.div = document.getElementById("jsLab");
            this.div.innerHTML = this.window;
            // document.getElementById("tool").innerHTML = this.tool;
            //получение варианта задания
            if(document.getElementById("preGeneratedCode") && document.getElementById("preGeneratedCode").value !== "")
            {
                let jsLab = document.getElementById("jsLab");
                let generatedVariant = JSON.parse(document.getElementById("preGeneratedCode").value);
                console.log(generatedVariant);

                const state = appInstance.state.updateState((state) => {
                    return {
                        ...state,
                        initialMatrix: [...generatedVariant.initialMatrix],
                    }
                });

                const render = (state) => {
                    console.log('state', state);
                    console.log(appInstance);
                    renderTemplate(jsLab, getHTML({...state}));
                    bindActionListeners(appInstance);
                };

                appInstance.subscriber.subscribe('render', render);

                // инициализируем первую отрисовку
                appInstance.subscriber.emit('render', appInstance.state.getState());
            }
        },

        getCondition: function(){},
        getResults: function(){
            return JSON.stringify(appInstance.state.getState());
        },
        calculateHandler: function(text, code){},
    }
}

var Vlab = init_lab();