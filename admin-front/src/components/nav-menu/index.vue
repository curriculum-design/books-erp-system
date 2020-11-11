<script>
import CommonUtils from '@/utils/CommonUtils'
const SL = window.sessionStorage
export default {
    props: ['navMenu'],
    render(h) {
        const getItem = (index, {menuName, url, children, iconClass}) => {
            if (children && children.length > 0) {
                return (
                    <el-submenu index={`${index}`}>
                        <template slot="title">
                            <i class={`${iconClass}`}></i>
                            {menuName}
                        </template>
                        {children.map((child, childIndex) => {
                            return getItem(`${index}-${childIndex}`, child)
                        })}
                    </el-submenu>
                )
            }
            // let clickHander = () => {
            //     try {
            //         this.$router.push('' + url)
            //     } catch (e) {
            //         this.$router.push({name: '404', query: {url: url, name: menuName}})
            //     }
            //     this.$emit('menuClick')
            // }
            const getUrl = url => {
                if (url.indexOf('/') !== 0) {
                    url = '/' + url
                }
                return url
            }
            const menuClickHandler = (data) => {
                // console.log('state', this.$store.state.currentMenu)
                SL.setItem('CURRENT_MENU_INDEX', index)
                this.$store.dispatch('CHANGE_MENU', data)
                this.$router.push(getUrl(data.url))
            }
            return (
                <el-menu-item index={`${index}`} on-click={() => menuClickHandler({menuName, url, index})}>
                    <router-link to={getUrl(url)} class="menu-link">{menuName}</router-link>
                </el-menu-item>
            )
        }
        return this.filterMenus && (
            <el-menu unique-opened={true} class="shanhs-menu-nav" default-active={this.defaultIndex}>
                <div class="menu-search text-center">
                    <input placeholder="请输入搜索内容" value={this.searchText} on-input={this.searchChangeHandler} class="menu-search-input"/>
                </div>
                {this.filterMenus.map((item, index) => {
                    return getItem(index, item)
                })}
            </el-menu>
        )
    },
    data() {
        return {
            searchText: '',
            defaultIndex: ''
        }
    },
    computed: {
        filterMenus() {
            let currentMenuIndex = SL.getItem('CURRENT_MENU_INDEX')
            if (currentMenuIndex) {
                this.defaultIndex = currentMenuIndex
            }
            if (this.searchText && this.navMenu) {
                const all = []
                const pushToAll = (menu) => {
                    all.push(menu)
                    if (menu.children && menu.children.length) {
                        menu.children.forEach(menu => {
                            pushToAll(menu)
                        })
                    }
                }
                this.navMenu.forEach(menu => {
                    pushToAll(menu)
                })
                const filterAll = all.filter(a => CommonUtils.likeMatch(a.menuName, this.searchText) && (!a.children || !a.children.length))
                return CommonUtils.arrayToTree(filterAll, 'id', 'parentId')
            } else {
                return this.navMenu
            }
        },
    },
    methods: {
        searchChangeHandler({target}) {
            this.searchText = target.value
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.shanhs-menu-nav{
    @menuColor1: #324057;
    @menuSelectedColor: #406eff;
    @menuHoverColor: #53657c;
    @menuNormalColor: #1f2d3d;
    .menu-search-input{
        &::placeholder{
            color: rgba(255, 255, 255, 0.5);
        }
        color: #fff;
        padding: 8px 5px;
        background: rgba(255, 255, 255, 0.3);
        border: 0;
        outline: none;
    }
    .menu-search{
        padding: 10px 5px;
    }
    .el-submenu .el-menu-item {
        background-color: @menuNormalColor;
    }
    .menu-link{
        text-decoration: none;
        color: #fff!important;
    }
    .el-menu-item{
        &.is-active{
            background: @menuSelectedColor;
            &.is-opened{
                background: @menuColor1;
            }
        }
    }
    .el-menu-item:hover {
        background: @menuHoverColor;
    }
    .el-submenu__title{
        color: #fff;
    }
    .el-submenu__title:hover{
        background: @menuHoverColor;
    }

    .el-submenu {
        .el-menu {
            .el-submenu__title{
                background: @menuNormalColor;
            }
        }
    }
    .el-submenu{
        background: @menuColor1;
    }
    .nav-menu{
        color: #fff;
        background: @menuColor1;
    }

    .el-menu {
        background: @menuColor1;
    }
}

</style>
