## Численный эксперимент

Были реализованы стек Трайбера и стэк Трайбера с элиминацией.

Ожидается что с увеличением числа потоков при сбалансированных операциях скорость выполнения Стека Трайбера по отношению к Стеку Трайбера с элиминацией ухудшиться. Такая теория возникает в следствии того, что алгоритм с элиминацией лучше распараллеливает вычисления за счет временного промежуточного хранения значений отдельно от самого стека, благодаря чему pop лолжен реже мешать выполнению push.

Для тестирования было проведено 10000 запусков. За один раз выполнялось 100 запусков, высчитывалось среднее время и сравнивалось, так 100 раз.

$TS$ - стек Трайбера

$TSWE$ - стек Трайбера с элиминацией

Изначально были запущены с помощью 2 корутин. В одной выполянлся push 10000 раз, в другой pop 10000 раз для каждой реализации. Результаты находяться в res.txt.

После 10000 запусков и исключения выбросов

$\frac {TS} {TSWE} \approx 2,38$

Что говорит о том, что в среднем при сбалансированных операциях push и pop стек Трайбера с элиминацией имеет выигрыш в скорости примерно в 2,4 раза.

Затем были запущены с помощью 3 корутин. В первой и второй выполянлся push 10000 раз, в третьей pop 10000 раз для каждой реализации. Результаты находяться в res3.txt.

После 10000 запусков и исключения выбросов

$\frac {TS} {TSWE} \approx 1,95$

Что говорит о том, что в среднем чем хуже сбалансированы операции push и pop, тем меньше стек Трайбера с элиминацией имеет выигрыш в скорости по отношению к обычному стеку Трайбера.


После был запуск с помощью 1 корутины. Выполянлся push 10000 раз для каждой реализации. Результаты находяться в res2.txt.

После 10000 запусков и исключения выбросов

$\frac {TS} {TSWE} \approx 1,44$

Из этого можно сделать вывод что с увеличением числа потоков при сбалансированных операциях скорость выполнения Стека Трайбера по отношению к Стеку Трайбера с элиминацией ухудшается, так как когда была дополнительно операция pop, коэффициент отношения ${TS}$ к ${TSWE}$ уже станет примерно 2,4.

При рандомных операциях $\frac {TS} {TSWE}$ хоть и $\approx 1,78$ точный вывод дать сложно, так как если операции push и pop перестанут быть сбалансированными, то и выигрыш во времени станет не однозначным, возможно даже замедление алгоритма, так как элиминация может потребовать несколько больше тактов на выполнение.
