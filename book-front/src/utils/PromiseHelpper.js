const runSyncFunc = async (arr) => {
    const res = await Promise.all(arr.map(async (syncFun) => await syncFun()))
    return res
}
const sliceArray = (array, size) => {
    var result = []
    for (var x = 0; x < Math.ceil(array.length / size); x++) {
        var start = x * size
        var end = start + size
        result.push(array.slice(start, end))
    }
    return result
}
const throttle = async(arr, Concurrency) => {
    const tempArr = sliceArray(arr, Concurrency)
    const res = []
    const go = async (tempArr, step) => {
        if (tempArr[step]) {
            res.concat(await runSyncFunc([tempArr[step]]))
        }
        step++
        if (tempArr[step]) {
            go(tempArr, step)
        }
    }
    await go(tempArr, 0)
    return res
}
Promise.throttle = throttle
const a = (times) => setTimeout(Promise(r => r(times)), times)
Promise.throttle([1, 2, 3, 4, 5, 6].map(async (times) => await a(times)), 3)
