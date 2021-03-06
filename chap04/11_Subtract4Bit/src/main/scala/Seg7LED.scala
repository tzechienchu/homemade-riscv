// See LICENSE for license details.

import chisel3._
import chisel3.util._

/** 7セグメントLED用のバンドル
  */
class Seg7LEDBundle extends Bundle {
  /** 各セグメントの点灯用。0〜7をCAからCGに対応させる事。0の時に点灯、1の時に消灯します。 */
  val cathodes = Output(UInt(7.W))
  /** 小数点用。0の時に点灯、1の時に消灯。 */
  val decimalPoint = Output(UInt(1.W))
  /** 桁の選択用。1の桁が点灯します。 */
  val anodes = Output(UInt(8.W))
}

/**
  * 7セグメントLED点灯モジュール(1桁版)
  */
class Seg7LED1Digit extends Module {
  val io = IO(new Bundle {
    val num = Input(UInt(4.W))
    val seg7led = new Seg7LEDBundle // 引数を取らないコンストラクタなので型名の後の括弧は省略してみた
  })

  // いちいちio.を書くのが面倒なので、別名定義する
  private val seg7led = io.seg7led
  private val num = io.num

  seg7led.cathodes := MuxCase("b111_1111".U, // デフォルト値(全部消灯)。16すべてのパターンが定義されるので使われない。
    Array(             // gfe_dcba の順序にcathodeが並ぶ
      (num === "h0".U) -> "b100_0000".U,
      (num === "h1".U) -> "b111_1001".U,
      (num === "h2".U) -> "b010_0100".U,
      (num === "h3".U) -> "b011_0000".U,
      (num === "h4".U) -> "b001_1001".U,
      (num === "h5".U) -> "b001_0010".U,
      (num === "h6".U) -> "b000_0010".U,
      (num === "h7".U) -> "b101_1000".U,
      (num === "h8".U) -> "b000_0000".U,
      (num === "h9".U) -> "b001_0000".U,
      (num === "ha".U) -> "b000_1000".U,
      (num === "hb".U) -> "b000_0011".U,
      (num === "hc".U) -> "b100_0110".U,
      (num === "hd".U) -> "b010_0001".U,
      (num === "he".U) -> "b000_0110".U,
      (num === "hf".U) -> "b000_1110".U))

  seg7led.decimalPoint := 1.U         // 小数点は点灯させない。
  seg7led.anodes := "b1111_1110".U    // 0桁目だけ点灯させる
}

object Seg7LED1Digit extends App {
  chisel3.Driver.execute(args, () => new Seg7LED1Digit)
}
