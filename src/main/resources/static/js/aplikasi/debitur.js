var daftarDebitur = new Vue({
    el: '#daftarDebitur',
    data: {
        dataDebitur : null,
        sudahDicari : false,
        searchDebitur: "",
        searchedKeyword: ""
    },
    methods: {
        cariDebitur: _.debounce(function(){
            var vm = this;
            vm.dataDebitur = null;
            vm.sudahDicari = false;

            if(vm.searchDebitur.trim().length < 1){
                vm.dataDebitur = null;
                return;
            }

            axios.get('/api/debitur/', {
                params: {
                    search: vm.searchDebitur
                }
            })
            .then(function (response) {
                vm.dataDebitur = response.data;
                vm.sudahDicari = true;
                vm.searchedKeyword = vm.searchDebitur;

            })
            .catch(function (error) {
                console.log(error);
                vm.sudahDicari = true;
                vm.searchedKeyword = vm.searchDebitur;
            });
        },1000),
        editDebitur: function(debitur){
            console.log(debitur);
            window.location.href = "form?id=" + debitur.id;
        }
    }
});