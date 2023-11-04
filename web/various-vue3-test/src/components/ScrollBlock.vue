<template>
  <div class="scroll-block" ref="scrollBarDom">
    <el-scrollbar :always="true">
      <slot></slot>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'

const props = defineProps({
  thumbWidth: String
})

const defaultValues = {
  thumbWidth: '6px'
}

const scrollBarDom = ref()

let hideScrollBarTask

onMounted(() => {
  let verticalBar = scrollBarDom.value.querySelector('.el-scrollbar__bar.is-vertical')
  let verticalThumb = verticalBar.querySelector('.el-scrollbar__thumb')
  setVerticalThumbFiller(verticalThumb)
  setVerticalThumbWidth(verticalBar, verticalThumb)
  disableVerticalThumbMouseEvent(verticalBar, verticalThumb)
  setScrollListener(verticalThumb)
})

function setVerticalThumbFiller(thumb) {
  let filler = document.createElement('div')
  filler.classList.add('filler')
  thumb.appendChild(filler)
}

function setVerticalThumbWidth(bar, thumb) {
  let thumbWidth = props.thumbWidth ?? defaultValues.thumbWidth
  bar.style.width = thumbWidth
  thumb.style.width = thumbWidth
}

function disableVerticalThumbMouseEvent(bar, thumb) {
  let disableEventCallback = e => e.stopPropagation()
  bar.addEventListener('mousedown', disableEventCallback, true)
  thumb.addEventListener('mousedown', disableEventCallback, true)
}

function setScrollListener(thumb) {
  let contentWrapper = scrollBarDom.value.querySelector('.el-scrollbar__wrap')
  let thumbFiller = thumb.querySelector('.filler')
  contentWrapper.addEventListener('scroll', () => {
    if(hideScrollBarTask != null) clearTimeout(hideScrollBarTask)
    if(!thumbFiller.classList.contains('show')) thumbFiller.classList.add('show')
    hideScrollBarTask = setTimeout(() => {
      thumbFiller.classList.remove('show')
    }, 2000)
  })
}
</script>

<style scoped lang="scss">
::v-deep(.el-scrollbar__bar.is-horizontal) {
  display: none;
}

::v-deep(.el-scrollbar__bar.is-vertical) {
  .el-scrollbar__thumb {
    background-color: unset;
    opacity: unset;
    cursor: unset;

    .filler {
      background-color: transparent;
      opacity: var(--el-scrollbar-opacity, 0.3);
      border-radius: inherit;
      height: 100%;
      width: 100%;
      transition: 0.3s background-color;
    }

    .filler.show {
      background-color: var(--el-scrollbar-bg-color, var(--el-text-color-secondary));
    }
  }

  .el-scrollbar__thumb:hover {
    background-color: unset;
    opacity: unset;
  }
}
</style>